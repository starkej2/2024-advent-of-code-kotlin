package common

data class Grid(private val elements: List<CharArray>) {
    val width = elements.maxBy { row -> row.size }.size
    val height = elements.size

    val coordinates: Sequence<Coordinate> = sequence {
        for (rowIndex: Int in elements.indices) {
            for (columnIndex: Int in elements[rowIndex].indices) {
                yield(Coordinate(x = columnIndex, y = rowIndex))
            }
        }
    }

    fun valueAt(x: Int, y: Int): Char = elements[y][x]
    fun valueAt(coordinate: Coordinate): Char = valueAt(coordinate.x, coordinate.y)

    fun indexOfFirst(vararg char: Char): Coordinate? = indexOfFirst(char.toSet())

    fun indexOfFirst(charsToFind: Set<Char>): Coordinate? {
        coordinates.forEach { coordinate ->
            if (charsToFind.contains(valueAt(coordinate))) {
                return coordinate
            }
        }
        return null
    }

    fun setValueAt(coordinate: Coordinate, value: Char): Grid {
        val newElements = elements.map { it.copyOf() }.toMutableList()
        newElements[coordinate.y][coordinate.x] = value
        return Grid(newElements)
    }

    fun print() {
        println("  ${(0 until width).joinToString(" ")}")
        elements.forEachIndexed { index, row ->
            println("$index ${row.joinToString(" ")}")
        }
    }
}

data class Coordinate(val x: Int, val y: Int) {
    override fun toString(): String = "($x,$y)"
}

