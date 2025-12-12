import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue

class Shape(val shape: List<String>) {
    override fun toString(): String = shape.toString()
    val cnt: Int = shape.sumOf { it.count { it == '#' } }
}
class Test(val width: Int, val height: Int, val presents: List<Int>) {
    override fun toString(): String = "($width, $height) - $presents"
}
fun main() {
    fun String.parseInput(): List<String> {
        return this.split("\\s+".toRegex())
    }

    fun part1(input: List<String>): Long {
        var readInput = input
        val shapes = mutableListOf<Shape>()
        var tests: List<Test>
        while (true) {
            val chunk = readInput.takeWhile { it.isNotEmpty() }
            readInput = readInput.drop(chunk.size + 1)
            if (readInput.isNotEmpty()) {
                shapes += Shape(chunk.drop(1))
            } else {
                tests = chunk.map {
                    val rawTests = it.toIntList()
                    Test(rawTests[0], rawTests[1], rawTests.drop(2))
                }
                break
            }
        }
        shapes.println()
        tests.println()
        return tests.count {
            val square = it.width * it.height
            val require = it.presents.mapIndexed { i, p -> shapes[i].cnt * p }.sum()
            square >= require
        }.toLong()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // Or read a large test input from the `src/Day12_test.txt` file:
    val testInput = readInput("Day12_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day12.txt` file.
    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}