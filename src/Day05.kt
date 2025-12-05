import kotlin.math.max

fun main() {

    fun String.asRange(): LongRange {
        val (l,r) = this.split('-')
        return LongRange(l.toLong(), r.toLong())
    }

    fun part1(input: List<String>): Long {
        val ranges = input.takeWhile { it.isNotEmpty() }.map{ it.asRange() }
        val data = input.takeLastWhile { it.isNotEmpty() }.map{ it.toLong() }
        val ans = data.count { ing -> ranges.any { ing in it } }
        return ans.toLong()
    }

    fun part2(input: List<String>): Long {
        val ranges = input.takeWhile { it.isNotEmpty() }.map{ it.asRange() }.toMutableList()
        ranges.sortWith(Comparator { a, b -> a.start.compareTo(b.start) })
        var ans = 0L
        var l = ranges.first().first
        var r = ranges.first().last
        for (cur in ranges) {
            if(cur.first <= r) {
                r = max(r, cur.last)
            } else {
                ans += r - l + 1L
                l = cur.first
                r = cur.last
            }
        }
        ans += r - l + 1L
        return ans
    }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInput("Day05_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}