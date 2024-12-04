fun main() {
    val input = readInput("Day03")
//    val input = readInput("Test")
    val corruptedMemory = input.joinToString("")
    println("[part 1] result = ${part1(corruptedMemory)}")
    println("[part 2] result = ${part2(corruptedMemory)}")
}

private fun part1(corruptedMemory: String): Int {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex(RegexOption.IGNORE_CASE)
    return regex.findAll(corruptedMemory)
        .map { result ->
            val (factor1, factor2) = result.destructured
            MultiplyOperation(factor1.toInt(), factor2.toInt())
        }
        .sumOf { it.product }
}

private fun part2(corruptedMemory: String): Int {
    return -1
}

data class MultiplyOperation(val factor1: Int, val factor2: Int) {
    val product = factor1 * factor2
}