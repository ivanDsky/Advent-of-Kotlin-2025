import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map {
            it.map { c -> c.digitToInt() }
        }
    }

    fun List<Int>.max2(): Int {
        val minPos = MutableList(10) { Int.MAX_VALUE }
        val maxPos = MutableList(10) { Int.MIN_VALUE }
        for (x in this.indices) {
            minPos[this[x]] = minOf(minPos[this[x]], x)
            maxPos[this[x]] = maxOf(maxPos[this[x]], x)
        }

        for (i in 9 downTo 0) {
            for (j in 9 downTo 0) {
                if (minPos[i] < maxPos[j]) { return 10 * i + j}
            }
        }

        return 0
    }

    fun update(input: String, digit: Char): String {
        var ans = input
        for (i in input.indices) {
            val tmp = digit + input.removeRange(i, i+1)
            if (tmp.toLong() > ans.toLong()) {
                ans = tmp
            }
        }
        return ans
    }

    fun String.max12(): Long {
        var subset = this.takeLast(12)
        for (i in length - 13 downTo 0) {
            subset = update(subset, this[i])
        }
        return subset.toLong().also { it.println()}
    }


    fun part1(input: List<String>): Long {
        val batteries = parseInput(input)
        return batteries.sumOf { it.max2() }.toLong()
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { it.max12() }
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInput("Day03_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}