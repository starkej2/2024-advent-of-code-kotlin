fun main() {
//    val input = readInput("Day04")
    val grid = readInput("Test").map { lines -> lines.toCharArray() }
    println("[part 1] result = ${part1(grid)}")
    println("[part 2] result = ${part2(grid)}")
}

private fun part1(grid: List<CharArray>): Int {
    return grid.countOccurrencesOf("XMAS")
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
//            println("no match: $currentLetter at (${nextRowIndex},${nextColumnIndex})")
            return false
        } else {
//            println("MATCH: $currentLetter at (${nextRowIndex},${nextColumnIndex})")
        }
    }

    // WIP clean up logs
//    println("found match! in direction $crawlDirection from $startPosition")
    return true
}

enum class CrawlDirection(private val rowDelta: Int, private val columnDelta: Int) {
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

private fun part2(grid: List<CharArray>): Int {
//    printMatrixWithCoordinates(grid) // WIP clean up
    return -1
}


data class GridPosition(val row: Int, val column: Int)

private fun printMatrixWithCoordinates(grid: List<CharArray>) {
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, letter ->
            print("$rowIndex,$columnIndex:$letter  ")
        }
        print("\n")
    }
}