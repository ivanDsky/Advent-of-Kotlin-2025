import com.microsoft.z3.ArithExpr
import com.microsoft.z3.ArithSort
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.IntSort
import com.microsoft.z3.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.ArrayDeque
import java.util.Queue
import kotlin.math.min


private fun Machine.fewestButtonPressesToTurnJoltageOn(): Int = with(Context()) {
    operator fun <R : ArithSort> ArithExpr<R>.plus(other: ArithExpr<R>) = mkAdd(this, other)
    fun Int.int() = mkInt(this)

    val optimization = mkOptimize()
    val buttonPresses = schematics.indices.map { mkIntConst("x$it") }

    for (presses in buttonPresses) {
        optimization.Add(mkGe(presses, 0.int()))
    }
    val totalPresses = buttonPresses.fold<IntExpr, ArithExpr<IntSort>>(0.int()) { acc, n ->
        acc + n
    }

    val joltages = joltage.map { 0.int() }.toMutableList<ArithExpr<IntSort>>()

    optimization.MkMinimize(totalPresses)
    for ((button, presses) in schematics.zip(buttonPresses)) {
        for (i in button) {
            joltages[i] = joltages[i] + presses
        }
    }

    for ((joltage, targetJoltage) in joltages.zip(joltage)) {
        optimization.Add(mkEq(joltage, targetJoltage.int()))
    }

    require(optimization.Check() == Status.SATISFIABLE)
    val totalPressesEvaluated = optimization.model.evaluate(totalPresses, false)
    require(totalPressesEvaluated is IntNum)
    return totalPressesEvaluated.int
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
                it.fewestButtonPressesToTurnJoltageOn()
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