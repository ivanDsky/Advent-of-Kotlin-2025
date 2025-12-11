fun main() {

    fun String.parseInput(): List<String> {
        return this.split("\\s+".toRegex())
    }

    val graph: MutableMap<String, List<String>> = mutableMapOf()


    val cache: MutableMap<String, Long> = mutableMapOf()
    fun dfs(current: String, dest: String): Long =
        cache.getOrPut(current) {
            if (current == dest) 1
            else (graph[current] ?: emptyList()).sumOf { dfs(it, dest) }
        }

    fun part2(input: List<String>): Long {
        input.forEach {
            val parsed = it.parseInput()
            graph[parsed.first().dropLast(1)] = parsed.drop(1)
        }

        cache.clear()
        val cnt1 = dfs("svr", "fft")
        cache.clear()
        val cnt2 = dfs("fft", "dac")
        cache.clear()
        val cnt3 = dfs("dac", "out")

        return cnt1 * cnt2 * cnt3
    }

    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readInput("Day11_test")
//    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    part2(input).println()
}