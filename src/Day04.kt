import kotlin.math.abs

fun main() {
    fun List<CharSequence>.isValid(i: Int, j: Int): Boolean {
        if(this[i][j] != '@') return false
        var cnt = 0
        for(delI in -1..1) {
            for(delJ in -1..1) {
                val ni = i + delI
                val nj = j + delJ
                if (ni !in this.indices || nj !in this[ni].indices) continue
                cnt += if (this[ni][nj] == '@') 1 else 0
            }
        }
        return cnt < 5
    }
    fun part1(input: List<String>): Long {
        var ans = 0L
        for (i in input.indices) {
            for (j in input[i].indices) {
                ans += if (input.isValid(i, j)) 1 else 0
            }
        }
        return ans
    }

    fun part1Mod(input: MutableList<StringBuilder>): Long {
        var ans = 0L
        for (i in input.indices) {
            for (j in input[i].indices) {
                ans += if (input.isValid(i, j)) 1 else 0
                if(input.isValid(i, j)) input[i][j] = '.'
            }
        }
        return ans
    }

    fun part2(input: MutableList<StringBuilder>): Long {
        var ans = 0L
        while (true) {
            val tmp = part1Mod(input)
            if(tmp == 0L) break
            ans += tmp
        }
        return ans
    }

    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    part1(testInput).println()
    part2(testInput.map { StringBuilder(it) }.toMutableList()).println()

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input.map { StringBuilder(it) }.toMutableList()).println()
}