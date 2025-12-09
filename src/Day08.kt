import kotlin.math.abs

fun main() {
    fun parseInput(input: String) : Vec3{
        val (x,y,z) = input.toIntList()
        return Vec3(x,y,z)
    }

    fun dist(a: Vec3, b: Vec3) : Long {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z)
    }

    fun part1(input: List<String>): Long {
        val coords = input.map { parseInput(it) }
        val sizes = mutableListOf<Vec2>()
        for (i in coords.indices) {
            for (j in i + 1 .. coords.lastIndex) {
                sizes.add(Vec2(i,j))
            }
        }
        sizes.sortWith(Comparator({ a, b ->
            dist(coords[a.i], coords[a.j]).compareTo(dist(coords[b.i], coords[b.j]))
        }))

        val groups = MutableList(coords.size) { it }

        fun getGroup(x: Int): Int {
            return if(groups[x] == x) x else getGroup(groups[x]).also { groups[x] = it }
        }

        fun merge(x: Int, y: Int) {
            val nx = getGroup(x)
            val ny = getGroup(y)
            groups[nx] = ny
        }

        var idx = 0
        var cnt = 0
        while(true) {
            if (cnt >= 999 || idx >= sizes.size) break
            val edge = sizes[idx++]
            val x = getGroup(edge.i)
            val y = getGroup(edge.j)
            if (x == y) continue
            merge(x,y)
            cnt++
        }

        val biggestGroups = mutableMapOf<Int, List<Int>>()

        for (i in groups.indices) {
            biggestGroups[getGroup(i)] = biggestGroups.getOrPut(getGroup(i)) { mutableListOf() } + i
        }

        val szs = biggestGroups.values.map { it.size }.sortedDescending().take(3)

        return szs.fold(1L) { acc, nx -> acc * nx }
    }

    fun part2(input: List<String>): Long {
        val coords = input.map { parseInput(it) }
        val sizes = mutableListOf<Vec2>()
        for (i in coords.indices) {
            for (j in i + 1 .. coords.lastIndex) {
                sizes.add(Vec2(i,j))
            }
        }
        sizes.sortWith(Comparator({ a, b ->
            dist(coords[a.i], coords[a.j]).compareTo(dist(coords[b.i], coords[b.j]))
        }))

        val groups = MutableList(coords.size) { it }

        fun getGroup(x: Int): Int {
            return if(groups[x] == x) x else getGroup(groups[x]).also { groups[x] = it }
        }

        fun merge(x: Int, y: Int) {
            val nx = getGroup(x)
            val ny = getGroup(y)
            groups[nx] = ny
        }

        var idx = 0
        var ans = 0L
        while(true) {
            if (idx >= sizes.size) break
            val edge = sizes[idx++]
            val x = getGroup(edge.i)
            val y = getGroup(edge.j)
            if (x == y) continue
            ans = coords[edge.i].x * coords[edge.j].x
            merge(x,y)
        }

        return ans
    }

    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")
    part1(testInput).println()
    part2(testInput).println()

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}