import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayDeque
import java.util.Queue
import kotlin.coroutines.CoroutineContext
import kotlin.math.min

fun String.toBitmask(): Int {
    var answer = 0
    for (char in this.reversed()) {
        answer = answer * 2 + if (char == '#') 1 else 0
    }
    return answer
}

fun List<Int>.toBitmask(): Int {
    var answer = 0
    for (idx in this) {
        answer += (1 shl idx)
    }
    return answer
}

data class Machine(
    val light: String,
    val schematics: List<List<Int>>,
    val joltage: List<Int>,
) {
    val lightBitmask = light.toBitmask()
    val schematicBitmask = schematics.map { it.toBitmask() }
}

class AsyncSolverX(machine: Machine) {
    val schems = machine.schematics.sortedByDescending { it.size }
    val checks = schems.map { it.toBitmask() }
    var ans = Int.MAX_VALUE / 2
    val memo = mutableMapOf<Pair<Int, List<Int>>, Int>()

    fun solve(sIdx: Int, joltage: MutableList<Int>, score: Int): Int {
        if (score + joltage.max() >= ans) return ans
        if (sIdx == schems.size) {
            if (joltage.all { it == 0 }) {
                ans = min(ans, score)
                return 0
            } else {
                return ans
            }
        }
        val tailScore = memo[sIdx to joltage]
        if (tailScore != null) {
            ans = min(ans, score + tailScore)
            return tailScore
        }

        val mx = schems[sIdx].minOf { joltage[it] }
        var tail = ans


        val isBad = joltage.indices.filter { joltage[it] > 0 }.any { id ->
            (sIdx..schems.lastIndex).all { i -> (checks[i] and (1 shl id)) == 0 }
        }
        if (isBad) return ans

        for (i in mx downTo 0) {
            schems[sIdx].forEach { joltage[it] -= i }
            tail = min(tail, solve(sIdx + 1, joltage, score + i) + i)
            schems[sIdx].forEach { joltage[it] += i }
        }
        memo[sIdx to joltage] = tail
        return tail
    }
}

fun main() {
    fun solveMachine(machine: Machine): Int {
        val queue: Queue<Int> = ArrayDeque()
        val dist = MutableList<Int>(1 shl machine.light.length) { Int.MAX_VALUE / 2 }
        dist[0] = 0
        queue.add(0)
        while (queue.isNotEmpty()) {
            val mask = queue.remove()
            if(mask == machine.lightBitmask) { return dist[mask] }
            for (s in machine.schematicBitmask) {
                if (dist[s xor mask] > dist[mask] + 1) {
                    dist[s xor mask] = dist[mask] + 1
                    queue.add(s xor mask)
                }
            }
        }
        return Int.MAX_VALUE
    }


    fun parseInput(input: String) : Machine {
        val light = "\\[(.*)]".toRegex().find(input)?.groupValues[1]!!
        val joltage = "\\{(.*)}".toRegex().find(input)?.value!!.toIntList()
        val schematics = "\\((.*?)\\)".toRegex().findAll(input).map { it.value.toIntList() }.toList()

        return Machine(light, schematics, joltage)
    }

    fun part1(input: List<String>): Long {
        val machines = input.map { parseInput(it) }
        return machines.sumOf { solveMachine(it) }.toLong()
    }

    val memo: MutableMap<List<Int>, Int> = mutableMapOf()

    fun prepare(machine: Machine, joltage: MutableList<Int>, dist: Int) {
        if (dist > 16) return
        if (dist >= (memo[joltage] ?: Int.MAX_VALUE)) return

        for (s in machine.schematics) {
            s.forEach { joltage[it]++ }
            prepare(machine, joltage, dist + 1)
            s.forEach { joltage[it]-- }
        }

        memo[joltage] = dist

        for (i in 2..(32/(dist + 1))) {
            var isGood = true
            for (idx in joltage.indices) {
                joltage[idx] = joltage[idx] * i
                if (joltage[idx] > machine.joltage[idx]) {isGood = false}
            }
            memo[joltage] = min(memo[joltage] ?: Int.MAX_VALUE, dist * i)
            for (idx in joltage.indices) {
                joltage[idx] = joltage[idx] / i
            }
            if (!isGood) break
        }
    }

    fun fullSolveMachine(schematics: List<List<Int>>, joltage: MutableList<Int>): Int {
        if (memo[joltage] != null) return memo[joltage]!!
        if (joltage.all { it == 0 }) {
            memo[joltage] = 0
            return 0
        }
        var ans = Int.MAX_VALUE / 2
        var bestAns = joltage.max()

        for (s in schematics) {
            s.forEach { joltage[it]-- }
            if (ans == bestAns) break
            if (joltage.all { it >= 0 })
                ans = min(ans, fullSolveMachine(schematics, joltage) + 1)
            s.forEach { joltage[it]++ }
        }
        memo[joltage] = ans
        return ans
    }

    fun fullSolve(machine: Machine): Int {
        val queue: Queue<List<Int>> = ArrayDeque()
        val dist = mutableMapOf<List<Int>, Int>()
        dist[machine.joltage] = 0
        queue.add(machine.joltage)
        var cnt = 0
        while (queue.isNotEmpty()) {
            cnt++
            val mask = queue.remove()
            if(mask.all { it == 0 }) {
                println("Cnt = $cnt")
                return dist[mask]!!
            }
            for (s in machine.schematics) {
                val joltage = mask.toMutableList()
                s.forEach { joltage[it]-- }
                if (joltage.any { it < 0 }) continue
                if ((dist[joltage] ?: Int.MAX_VALUE) > dist[mask]!! + 1) {
                    dist[joltage] = dist[mask]!! + 1
                    queue.add(joltage)
                }
            }
        }
        return Int.MAX_VALUE
    }

    fun part2(input: List<String>): Long {
        val machines = input.map { parseInput(it) }
        val scope = CoroutineScope(Dispatchers.Default)
        val answers = machines.map { it ->
            scope.async {
                val solver = AsyncSolverX(it)
                solver.solve(0, it.joltage.toMutableList(), 0)
                println("--${solver.ans}")
                solver.ans
            }
        }

        return runBlocking { answers.awaitAll().sum().toLong() }

        return machines.sumOf {
            memo.clear()
            prepare(it, it.joltage.map { 0 }.toMutableList(), 0)
            println("-p- ${memo.size}")
            fullSolveMachine(it.schematics.sortedByDescending { it.size }, it.joltage.toMutableList()).also { println("-s- $it") }
//            fullSolve(it)
        }.toLong()
    }

    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}