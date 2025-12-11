import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue

data class DistPair(val key: String, val dist: Long) : Comparable<DistPair> {
    override fun compareTo(other: DistPair): Int { return dist.compareTo(other.dist) }
}

fun main() {

    fun String.parseInput(): List<String> {
        return this.split("\\s+".toRegex())
    }

    fun part1(input: List<String>): Long {
        val graph: MutableMap<String, List<String>> = mutableMapOf()
        input.forEach {
            val parsed = it.parseInput()
            graph[parsed.first().dropLast(1)] = parsed.drop(1)
        }

        val dp: MutableMap<String, Int> = mutableMapOf()
        val queue: Queue<String> = LinkedList()
        dp["you"] = 1
        queue += "you"
        while (queue.isNotEmpty()) {
            val curr = queue.poll()
            for (key in graph[curr] ?: emptyList()) {
                val isNew = dp[key] == null
                dp[key] = dp.getOrDefault(key, 0) + dp[curr]!!
                if(isNew) queue.add(key)
            }
        }

        return dp["out"]!!.toLong()
    }

    fun part2(input: List<String>): Long {
        val graph: MutableMap<String, List<String>> = mutableMapOf()
        input.forEach {
            val parsed = it.parseInput()
            graph[parsed.first().dropLast(1)] = parsed.drop(1)
        }

        val dp: MutableMap<String, Array<Long>> = mutableMapOf()
        val queue: Queue<Pair<String, Int>> = LinkedList()
        val visited: MutableSet<Pair<String, Int>> = mutableSetOf()
        dp["svr"] = arrayOf(1,0,0,0)
        queue += "svr" to 0
        visited += "svr" to 0
        while (queue.isNotEmpty()) {
            val (curr, mask) = queue.poll()
            for (key in graph[curr] ?: emptyList()) {
                dp.putIfAbsent(key, arrayOf(0,0,0,0))
                val nmask = mask + (if(key == "fft") 1 else 0) + (if(key == "dac") 2 else 0)
                if(!visited.contains(key to nmask)) {
                    queue.add(key to nmask)
                    visited += key to nmask
                }
                dp[key]!![nmask] += dp[curr]!![mask]
            }
        }

        return dp["out"]!![3]
    }

    fun part2X(input: List<String>): Long {
        val graph: MutableMap<String, List<String>> = mutableMapOf()
        input.forEach {
            val parsed = it.parseInput()
            graph[parsed.first().dropLast(1)] = parsed.drop(1)
        }

        val start = "fft"
        val finish = "dac"

        val dp: MutableMap<String, Long> = mutableMapOf()
        val pq = PriorityQueue<DistPair>()

        dp[start] = 1
        pq += DistPair(start, 0)
        while (pq.isNotEmpty()) {
            val (curr, dist) = pq.poll()
            for (key in graph[curr] ?: emptyList()) {
                val isNew = dp[key] == null
                dp[key] = dp.getOrDefault(key, 0) + dp[curr]!!
                if(isNew) pq.add(DistPair(key, dist + 1))
            }
        }

        return (dp[finish]?:0).toLong()
    }

    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readInput("Day11_test")
//    part1(testInput).println()
    part2X(testInput).println()

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2X(input).println()
}
//1058
//4058555
//5317
//1058*5317*4058555
//43134108570
//21778586267438
//45635886867060
//329630970049252