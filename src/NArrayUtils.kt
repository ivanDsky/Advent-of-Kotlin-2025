import kotlin.math.sqrt

data class Vec2(val x: Long, val y: Long) {
    constructor(x: Int, y: Int): this(x.toLong(), y.toLong())

    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)
    operator fun times(other: Long) = Vec2(x * other, y * other)
    override fun toString(): String = "($x, $y)"
}

fun Vec2.dot(other: Vec2): Long = x * other.x + y * other.y
fun Vec2.length() = sqrt((x * x + y * y).toDouble())

val Vec2.i: Int get() = x.toInt()
val Vec2.j: Int get() = y.toInt()

operator fun<T> List<List<T>>.get(vec2: Vec2) = this[vec2.i][vec2.j]
operator fun List<CharSequence>.get(vec2: Vec2) = this[vec2.i][vec2.j]
operator fun<T> List<MutableList<T>>.set(vec2: Vec2, value: T) { this[vec2.i][vec2.j] = value }
infix fun<T> Vec2.inBounds(list: List<List<T>>) = i in list.indices && j in list[0].indices
infix fun<T> Vec2.notInBounds(list: List<List<T>>) = !inBounds(list)

data class Vec3(val x: Long, val y: Long, var z: Long) {
    constructor(x: Int, y: Int, z: Int): this(x.toLong(), y.toLong(), z.toLong())
    operator fun plus(other: Vec3) = Vec3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vec3) = Vec3(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Long) = Vec3(x * other, y * other, z * other)
    override fun toString(): String = "($x, $y, $z)"
}
val Vec3.i: Int get() = x.toInt()
val Vec3.j: Int get() = y.toInt()
val Vec3.k: Int get() = z.toInt()
val Vec3.xy: Vec2 get() = Vec2(x, y)

operator fun<T> List<List<List<T>>>.get(vec3: Vec3) = this[vec3.i][vec3.j][vec3.k]
operator fun<T> List<List<MutableList<T>>>.set(vec3: Vec3, value: T) { this[vec3.i][vec3.j][vec3.k] = value }