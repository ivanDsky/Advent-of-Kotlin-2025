import java.awt.Polygon
import java.awt.Rectangle
import java.awt.geom.Rectangle2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun area(a: Vec2, b: Vec2): Long = (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1)
    fun part1(input: List<String>): Long {
        val points = input.map {
            val (a, b) = it.split(",")
            Vec2(a.toInt(), b.toInt())
        }
        var ans = 0L
        for(a in points) {
            for(b in points) {
                ans = max(ans, area(a, b))
            }
        }
        return ans
    }

    fun isValid(a: Vec2, b: Vec2, p: Polygon): Boolean {
        val rect = Rectangle2D.Double(
            min(a.x,b.x) + 0.5,
            min(a.y,b.y) + 0.5,
            abs(a.x - b.x) - 1.0,
            abs(a.y - b.y) - 1.0
        )
        return p.contains(rect)
    }

    fun part2(input: List<String>): Long {
        val points = input.map {
            val (a, b) = it.split(",")
            Vec2(a.toInt(), b.toInt())
        }
        var ans = 0L
        val p = Polygon(
            points.map { it.x.toInt() }.toIntArray(),
            points.map { it.y.toInt() }.toIntArray(),
            points.size
        )
        for(a in points) {
            for(b in points) {
                if (area(a,b) > ans && isValid(a,b,p)) {
                    ans = max(ans, area(a,b))
                }
            }
        }
        return ans
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}