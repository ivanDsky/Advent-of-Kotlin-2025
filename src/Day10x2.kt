import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.ArrayDeque
import java.util.Queue
import kotlin.math.min

class AsyncSolverXX(machine: Machine) {
    val schems = machine.schematics.sortedByDescending { it.size }
    val checks = schems.map { it.toBitmask() }
    var ans = Int.MAX_VALUE / 2
    var cnt = 0

    private val joltage = machine.joltage.toMutableList()
    private val nonNullIndices = mutableSetOf<Int>().also {
        it.addAll(joltage.indices)
    }

    fun solve(sIdx: Int, score: Int): Int {
        cnt++
        if (score + joltage.max() >= ans) return ans
        if (sIdx == schems.size) {
            if (nonNullIndices.isEmpty()) {
                ans = min(ans, score)
                return 0
            } else {
                return ans
            }
        }

        val mx = schems[sIdx].minOf { joltage[it] }
        var tail = ans


        val isBad = nonNullIndices.any { id ->
            (sIdx..schems.lastIndex).all { i -> (checks[i] and (1 shl id)) == 0 }
        }
        if (isBad) return ans

        for (i in mx downTo 0) {
            schems[sIdx].forEach {
                joltage[it] -= i
                if (joltage[it] <= 0) nonNullIndices.remove(it)
            }
            tail = min(tail, solve(sIdx + 1, score + i) + i)
            schems[sIdx].forEach {
                joltage[it] += i
                if (joltage[it] > 0) nonNullIndices.add(it)
            }
        }
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

    fun part2(input: List<String>): Long {
        val machines = input.map { parseInput(it) }
        val scope = CoroutineScope(Dispatchers.Default)
        val answers = machines.map { it ->
            scope.async {
                val solver = AsyncSolverXX(it)
                solver.solve(0, 0)
                println("--${solver.ans} - ${solver.cnt}")
                solver.ans
            }
        }

        return runBlocking { answers.awaitAll().sum().toLong() }
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