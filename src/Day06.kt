import common.Grid
import common.readInput

fun main() {
    val testInput = readInput("Day06_test")
    val realInput = readInput("Day06")

    println("[part 1] TEST result = ${part1(testInput)}")
//    println("[part 2] TEST result = ${part2(testInput)}\n")

//    println("[part 1] result = ${part1(realInput)}")
//    println("[part 2] result = ${part2(realInput)}")
}

private fun part1(input: List<String>): Int {
    val grid = Grid(input.map { it.toCharArray() })
    val guardCoord = grid.indexOfFirst('<', 'v', '^', '>')
    println("guard coord = $guardCoord --- orientation = ${guardCoord?.let { grid.valueAt(it) }}")
    return -1
}

private fun part2(input: List<String>): Int {
    return -1
}
