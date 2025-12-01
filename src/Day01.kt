import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): List<Int> {
        return input.map {
            val dir = it[0]
            val abs = it.drop(1).toInt()
            abs * (if (dir == 'L') 1 else -1)
        }
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)
        var cnt = 50
        var ans = 0
        for (del in parsedInput) {
            cnt = (cnt + del + 100) % 100
            if (cnt == 0) ans++
        }
        return ans
    }

    fun part2(input: List<String>): Int {
        val parsedInput = parseInput(input)
        var cnt = 50
        var ans = 0
        for (del in parsedInput) {
            repeat(abs(del)){
                if(del > 0) cnt++ else cnt--
                cnt %= 100
                if (cnt == 0) ans++
            }
        }
        return ans
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}