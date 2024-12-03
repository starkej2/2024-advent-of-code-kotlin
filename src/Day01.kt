import kotlin.math.abs

fun main() {
    val input = readInput("Day01")
    val (leftColumn, rightColumn) = input.splitToLeftAndRightColumns()
    println("part 1 result = ${part1(leftColumn, rightColumn)}")
    println("part 2 result = ${part2(leftColumn, rightColumn)}")
}

private fun part1(leftColumn: List<Int>, rightColumn: List<Int>): Int {
    return leftColumn.sorted().zip(rightColumn.sorted())
        .map { (leftValue, rightValue) -> LocationIdPair(leftValue, rightValue) }
        .sumOf { it.distance }
}

private fun part2(leftColumn: List<Int>, rightColumn: List<Int>): Int {
    val frequenciesByValue = rightColumn.groupingBy { it }.eachCount()
    val totalSimilarityScore = leftColumn.sumOf { leftValue ->
        leftValue * frequenciesByValue.getOrDefault(leftValue, 0)
    }
    return totalSimilarityScore
}

private fun List<String>.splitToLeftAndRightColumns(): Pair<List<Int>, List<Int>> {
    return this
        .map { line -> line.substringBefore(" ").toInt() to line.substringAfterLast(" ").toInt() }
        .unzip()
}

private data class LocationIdPair(val id1: Int, val id2: Int) {
    val distance: Int = abs(id2 - id1)
}