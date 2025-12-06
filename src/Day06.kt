import kotlin.math.max

fun main() {

    fun parseInput(input: String): List<Long> {
        return input.toLongList()
    }

    fun part1(input: List<String>): Long {
        val table = input.dropLast(1).map(::parseInput)
        val lastRow = input.last().split("\\s+".toRegex())
        var ans = 0L
        for (i in table[0].indices) {
            val isAdd = lastRow[i].contains('+')
            var acc = if (isAdd) 0L else 1L
            for (j in table.indices) {
                if (isAdd) {
                    acc += table[j][i]
                } else {
                    acc *= table[j][i]
                }
            }
            ans += acc
        }
        return ans
    }

    fun part2(input: List<String>): Long {
        val table = input.dropLast(1)
        val lastRow = input.last().split("\\s+".toRegex())
        val sets = mutableListOf<MutableList<Long>>()
        sets.add(mutableListOf())

        val sz = table.maxOf { it.length }
        for (j in 0..<sz) {
            val x = buildString {
                for(i in table.indices) {
                    if(table[i].getOrNull(j) == ' ')continue
                    if(table[i].getOrNull(j) == null)continue
                    append(table[i][j])
                }
            }
            if (x.isBlank()) {
                sets.add(mutableListOf())
            } else {
                sets.last() += x.toLong()
            }
        }

        var ans = 0L
        for (i in lastRow.indices) {
            ans += if (lastRow[i] == "+") sets[i].sum()
            else sets[i].fold(1L) { acc, i -> acc * i }
        }

        return ans
    }

    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day06_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}