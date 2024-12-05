fun main() {
//    val input = readInput("Day04")
    val input = readInput("Test")
    println("[part 1] result = ${part1(input)}")
    println("[part 2] result = ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val grid: List<CharArray> = input.map { lines -> lines.toCharArray() }
    printMatrixWithCoordinates(grid) // WIP clean up

    var count = 0
    val searchWord = "XMAS"
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, letter ->
            if (letter == searchWord.first()) {
                // WIP let's improve this
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.UP)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.UP_RIGHT)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.RIGHT)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.DOWN_RIGHT)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.DOWN)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.DOWN_LEFT)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.LEFT)) {
                    count++
                }
                if (grid.wordExists(searchWord, rowIndex, columnIndex, CrawlDirection.UP_LEFT)) {
                    count++
                }
            }
        }
    }

    return count
}

private fun List<CharArray>.wordExists(
    word: String,
    startRowIndex: Int,
    startColumnIndex: Int,
    crawlDirection: CrawlDirection,
): Boolean {
    for (i in word.indices) {
        // WIP can this be moved to the enum?
        val nextColumnIndex = when (crawlDirection) {
            CrawlDirection.UP, CrawlDirection.DOWN -> startColumnIndex
            CrawlDirection.RIGHT, CrawlDirection.UP_RIGHT, CrawlDirection.DOWN_RIGHT -> startColumnIndex + i
            CrawlDirection.LEFT, CrawlDirection.UP_LEFT, CrawlDirection.DOWN_LEFT -> startColumnIndex - i
        }

        val nextRowIndex = when (crawlDirection) {
            CrawlDirection.UP, CrawlDirection.UP_RIGHT, CrawlDirection.UP_LEFT -> startRowIndex - i
            CrawlDirection.RIGHT, CrawlDirection.LEFT -> startRowIndex
            CrawlDirection.DOWN, CrawlDirection.DOWN_RIGHT, CrawlDirection.DOWN_LEFT -> startRowIndex + i
        }

        if ((nextRowIndex < 0 || nextRowIndex > this.lastIndex) ||
            (nextColumnIndex < 0 || nextColumnIndex > this[startRowIndex].lastIndex)
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

    println("found match! in direction $crawlDirection from ($startRowIndex,$startColumnIndex)")
    return true
}

enum class CrawlDirection {
    UP,
    UP_RIGHT,
    RIGHT,
    DOWN_RIGHT,
    DOWN,
    DOWN_LEFT,
    LEFT,
    UP_LEFT,
}

private fun part2(input: List<String>): Int {
    return -1
}

private fun printMatrixWithCoordinates(grid: List<CharArray>) {
    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, letter ->
            print("$rowIndex,$columnIndex:$letter  ")
        }
        print("\n")
//        println("row $index: ${row.joinToString("")}")
    }
}