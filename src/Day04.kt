fun main() {
    val grid = readInput("Day04").map { lines -> lines.toCharArray() }
    println("[part 1] result = ${part1(grid)}")
    println("[part 2] result = ${part2(grid)}")
}

private fun part1(grid: List<CharArray>): Int {
    return grid.countOccurrencesOf("XMAS")
}

private fun part2(grid: List<CharArray>): Int {
    return grid.countXShapedOccurrencesOf("MAS")
}

private fun List<CharArray>.countXShapedOccurrencesOf(
    word: String,
): Int {
    if (word.length.isEven) {
        throw IllegalArgumentException("Word length must be odd")
    }

    val middleLetter = word[word.length / 2]
    var xShapedWordCount = 0

    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, letter ->
            if (letter == middleLetter) {
                val diagonalDirections = setOf(
                    CrawlDirection.UP_RIGHT,
                    CrawlDirection.DOWN_RIGHT,
                    CrawlDirection.DOWN_LEFT,
                    CrawlDirection.UP_LEFT
                )

                val middleLetterPosition = GridPosition(rowIndex, columnIndex)
                val partialXCount = diagonalDirections.count { direction ->
                    val wordStartPosition = direction.calculatePosition(middleLetterPosition, -1)
                    this.wordExists(word, wordStartPosition, direction)
                }

                if (partialXCount == 2) {
                    xShapedWordCount++
                }
            }
        }
    }
    return xShapedWordCount
}

private fun List<CharArray>.countOccurrencesOf(
    word: String,
): Int {
    var count = 0
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, letter ->
            val currentPosition = GridPosition(rowIndex, columnIndex)
            if (letter == word.first()) {
                CrawlDirection.entries.forEach { direction ->
                    if (this.wordExists(word, currentPosition, direction)) {
                        count++
                    }
                }
            }
        }
    }
    return count
}

private fun List<CharArray>.wordExists(
    word: String,
    startPosition: GridPosition,
    crawlDirection: CrawlDirection,
): Boolean {
    for (i in word.indices) {
        val nextColumnIndex = crawlDirection.calculatePosition(startPosition, i).column
        val nextRowIndex = crawlDirection.calculatePosition(startPosition, i).row

        if ((nextRowIndex < 0 || nextRowIndex > this.lastIndex) ||
            (nextColumnIndex < 0 || nextColumnIndex > this[startPosition.row].lastIndex)
        ) {
            return false
        }

        val currentLetter = this[nextRowIndex][nextColumnIndex]
        if (currentLetter != word[i]) {
            return false
        }
    }
    return true
}

private enum class CrawlDirection(private val rowDelta: Int, private val columnDelta: Int) {
    UP(rowDelta = -1, columnDelta = 0),
    UP_RIGHT(rowDelta = -1, columnDelta = 1),
    RIGHT(rowDelta = 0, columnDelta = 1),
    DOWN_RIGHT(rowDelta = 1, columnDelta = 1),
    DOWN(rowDelta = 1, columnDelta = 0),
    DOWN_LEFT(rowDelta = 1, columnDelta = -1),
    LEFT(rowDelta = 0, columnDelta = -1),
    UP_LEFT(rowDelta = -1, columnDelta = -1),
    ;

    fun calculatePosition(startPosition: GridPosition, stepSize: Int): GridPosition {
        return GridPosition(
            row = startPosition.row + stepSize * rowDelta,
            column = startPosition.column + stepSize * columnDelta
        )
    }
}

private data class GridPosition(val row: Int, val column: Int)

private val Int.isEven: Boolean
    get() = this % 2 == 0