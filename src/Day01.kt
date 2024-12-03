import kotlin.math.abs

fun main() {
    val input = readInput("Day01")
    println("part 1 result = ${part1(input)}")
    println("part 2 result = ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val leftList = mutableListOf(input.size)
    val rightList = mutableListOf(input.size)
    input.forEach { line ->
        leftList.add(line.substringBefore(" ").toInt())
        rightList.add(line.substringAfterLast(" ").toInt())
    }

    val sortedLeftList = leftList.sorted()
    val sortedRightList = rightList.sorted()
    val idPairs = (0..input.size).map { LocationIdPair(sortedLeftList[it], sortedRightList[it]) }
    return idPairs.sumOf { it.distance }
}

private fun part2(input: List<String>): Int {
    return input.size // WIP implement
}

data class LocationIdPair(val id1: Int, val id2: Int) {
    val distance: Int = abs(id2 - id1)
}