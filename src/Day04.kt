fun main() {
    val grid = readInput("Day04").map { lines -> lines.toCharArray() }
    println("[part 1] result = ${part1(grid)}")
    println("[part 2] result = ${part2(grid)}")
}

private fun part1(grid: List<CharArray>): Int {
    return grid.countOccurrencesOf("XMAS")
}

private fun part2(grid: List<CharArray>): Int {
    return grid.countCrossShapedOccurrencesOf("MAS")
}

private fun List<CharArray>.countCrossShapedOccurrencesOf(
    word: String,
): Int {
    if (word.length.isEven) {
        throw IllegalArgumentException("Word length must be odd")
    }

    val middleLetter = word[word.length / 2]
    var crossShapedWordCount = 0

    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, letter ->
            if (letter == middleLetter) {
                val intercardinalDirections = setOf(
                    CrawlDirection.NORTHEAST,
                    CrawlDirection.SOUTHEAST,
                    CrawlDirection.SOUTHWEST,
                    CrawlDirection.NORTHWEST
                )

                val middleLetterPosition = GridPosition(rowIndex, columnIndex)
                val partialCrossCount = intercardinalDirections.count { direction ->
                    val wordStartPosition = direction.calculatePosition(middleLetterPosition, -1)
                    this.wordExists(word, wordStartPosition, direction)
                }

                if (partialCrossCount == 2) {
                    crossShapedWordCount++
                }
            }
        }
    }
    return crossShapedWordCount
}

private fun List<CharArray>.countOccurrencesOf(
    word: String,
): Int {
    var count = 0
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, _ ->
            val currentPosition = GridPosition(rowIndex, columnIndex)
            CrawlDirection.entries.forEach { direction ->
                if (this.wordExists(word, currentPosition, direction)) {
                    count++
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
    NORTH(rowDelta = -1, columnDelta = 0),
    NORTHEAST(rowDelta = -1, columnDelta = 1),
    EAST(rowDelta = 0, columnDelta = 1),
    SOUTHEAST(rowDelta = 1, columnDelta = 1),
    SOUTH(rowDelta = 1, columnDelta = 0),
    SOUTHWEST(rowDelta = 1, columnDelta = -1),
    WEST(rowDelta = 0, columnDelta = -1),
    NORTHWEST(rowDelta = -1, columnDelta = -1),
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