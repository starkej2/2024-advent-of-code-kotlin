import common.Coordinate
import common.Grid
import common.readInput

fun main() {
//    val grid = readInput("Day04_test").map { lines -> lines.toCharArray() }
    val grid = Grid(readInput("Day04").map { lines -> lines.toCharArray() })
    println("[part 1] result = ${part1(grid)}")
    println("[part 2] result = ${part2(grid)}")
}

private fun part1(grid: Grid): Int {
    return grid.countOccurrencesOf("XMAS")
}

private fun part2(grid: Grid): Int {
    return grid.countCrossShapedOccurrencesOf("MAS")
}

private fun Grid.countCrossShapedOccurrencesOf(
    word: String,
): Int {
    if (word.length.isEven) {
        throw IllegalArgumentException("Word length must be odd")
    }

    val middleLetter = word[word.length / 2]
    var crossShapedWordCount = 0

    coordinates.forEach { coordinate ->
        if (valueAt(coordinate) == middleLetter) {
            val intercardinalDirections = setOf(
                CrawlDirection.NORTHEAST,
                CrawlDirection.SOUTHEAST,
                CrawlDirection.SOUTHWEST,
                CrawlDirection.NORTHWEST
            )

            val middleLetterPosition = coordinate
            val partialCrossCount = intercardinalDirections.count { direction ->
                val wordStartPosition = direction.calculatePosition(middleLetterPosition, -1)
                if (wordStartPosition != null) {
                    this.wordExists(word, wordStartPosition, direction)
                } else {
                    false
                }
            }

            if (partialCrossCount == 2) {
                crossShapedWordCount++
            }
        }
    }
    return crossShapedWordCount
}

private fun Grid.countOccurrencesOf(
    word: String,
): Int {
    var count = 0
    coordinates.forEach { currentPosition ->
        CrawlDirection.entries.forEach { direction ->
            if (this.wordExists(word, currentPosition, direction)) {
                count++
            }
        }
    }
    return count
}

private fun Grid.wordExists(
    word: String,
    startPosition: Coordinate,
    crawlDirection: CrawlDirection,
): Boolean {
    for (i in word.indices) {
        val nextPosition = crawlDirection.calculatePosition(startPosition, i) ?: return false
        if (nextPosition !in coordinates) {
            return false
        }
        val currentLetter = valueAt(nextPosition)
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

    fun calculatePosition(startPosition: Coordinate, stepSize: Int): Coordinate? {
        val newX = (startPosition.x + stepSize * rowDelta).takeIf { it >= 0 }
        val newY = (startPosition.y + stepSize * columnDelta).takeIf { it >= 0 }
        return if (newX != null && newY != null) {
            Coordinate(newX, newY)
        } else {
            null
        }
    }
}

private val Int.isEven: Boolean
    get() = this % 2 == 0