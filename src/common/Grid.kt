package common

data class Grid(private val elements: List<CharArray>) {
    val coordinates: Sequence<Coordinate> = sequence {
        for (rowIndex: Int in elements.indices) {
            for (columnIndex: Int in elements[rowIndex].indices) {
                yield(Coordinate(x = columnIndex, y = rowIndex))
            }
        }
    }

    fun valueAt(x: Int, y: Int): Char = elements[y][x]
    fun valueAt(coordinate: Coordinate): Char = valueAt(coordinate.x, coordinate.y)

    fun indexOfFirst(vararg char: Char): Coordinate? {
        val charsToFind = char.toSet()
        coordinates.forEach { coordinate ->
            if (charsToFind.contains(valueAt(coordinate))) {
                return coordinate
            }
        }
        return null
    }
}

data class Coordinate(val x: Int, val y: Int) {
    override fun toString(): String = "($x,$y)"
}