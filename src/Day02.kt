import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): List<LongRange> {
        return input[0].split(',').map {
            val (l,r) = it.split('-')
            l.toLong()..r.toLong()
        }
    }

    fun Long.isInvalid(): Boolean {
        val x = this.toString()
        if (x.length % 2 != 0) return false
        val a = x.take(x.length / 2)
        val b = x.takeLast(x.length / 2)
        return a == b
    }

    fun Long.isInvalid2(): Boolean {
        val x = this.toString()
        for (i in 1..<x.length) {
            if(x.length % i != 0) continue
            var isGood = true
            for(j in i..<x.length) {
                if(x[j] != x[j % i]) isGood = false
            }
            if(isGood) return true
        }
        return false
    }

    fun part1(input: List<String>): Long {
        val parsedInput = parseInput(input)
        return parsedInput.sumOf {
            var sum = 0L
            for (x in it) {
                if (x.isInvalid()) sum += x
            }
            sum
        }
    }

    fun part2(input: List<String>): Long {
        val parsedInput = parseInput(input)
        return parsedInput.sumOf {
            var sum = 0L
            for (x in it) {
                if (x.isInvalid2()) sum += x
            }
            sum
        }
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}