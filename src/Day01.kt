import kotlin.math.abs

fun main() {
    val input = readInput("Day01")
    println("part 1 result = ${part1(input)}")
    println("part 2 result = ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val (leftList, rightList) = input.splitToLeftAndRightList()
    val sortedLeftList = leftList.sorted()
    val sortedRightList = rightList.sorted()
    val idPairs = input.indices.map { LocationIdPair(sortedLeftList[it], sortedRightList[it]) }
    return idPairs.sumOf { it.distance }
}

private fun part2(input: List<String>): Int {
    val (leftList, rightList) = input.splitToLeftAndRightList()
    val totalSimilarityScore = leftList.sumOf { leftValue ->
        leftValue * rightList.count { it == leftValue }
    }
    return totalSimilarityScore
}

private fun List<String>.splitToLeftAndRightList(): Pair<List<Int>, List<Int>> {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()
    this.forEach { line ->
        leftList.add(line.substringBefore(" ").toInt())
        rightList.add(line.substringAfterLast(" ").toInt())
    }
    return Pair(leftList, rightList)
}

private data class LocationIdPair(val id1: Int, val id2: Int) {
    val distance: Int = abs(id2 - id1)
}