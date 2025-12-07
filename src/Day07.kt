fun main() {
    var cnt = 0L
    fun String.splitBeam(input: Map<Int, Long>): Map<Int, Long> {
        val ret = mutableMapOf<Int, Long>()
        for ((i,ways) in input) {
            if (this[i] == '^') {
                cnt++
                if (i > 0) ret[i - 1] = (ret[i - 1] ?: 0) + ways
                if (i + 1 < this.length)  ret[i + 1] = (ret[i + 1] ?: 0) + ways
            } else {
                ret[i] = (ret[i] ?: 0) + ways
            }
        }
        return ret
    }

    fun part1(input: List<String>): Long {
        cnt = 0
        var pos = input[0].indexOfFirst { it == 'S' }
        var set = mapOf(pos to 1L)
        input.drop(1).forEach {
            set = it.splitBeam(set)
        }
        return cnt
    }

    fun part2(input: List<String>): Long {
        cnt = 0
        var pos = input[0].indexOfFirst { it == 'S' }
        var set = mapOf(pos to 1L)
        input.drop(1).forEach {
            set = it.splitBeam(set)
        }
        return set.values.sum()
    }

    // Or read a large test input from the `src/Day07_test.txt` file:
    val testInput = readInput("Day07_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}