import Equation.Operator
import common.readInput

fun main() {
    val testInput = readInput("Day07_test")
    val realInput = readInput("Day07")

    println("[part 1] TEST result = ${part1(testInput)}")
//    println("[part 2] TEST result = ${part2(testInput)}\n")

    println("[part 1] result = ${part1(realInput)}")
//    println("[part 2] result = ${part2(realInput)}")
}

private fun part1(inputLines: List<String>): Long {
    return parseInput(inputLines)
        .filter { it.isValid }
        .sumOf { it.targetValue }
}

private fun part2(inputLines: List<String>): Long {
    return -1
}

private fun parseInput(inputLines: List<String>): List<Equation> {
    return inputLines.map {
        val (rawTestValue, rawNumbers) = it.split(":")
        Equation(
            targetValue = rawTestValue.trim().toLong(),
            numbers = rawNumbers.trim().split(" ").map { it.toLong() },
        )
    }
}

private data class Equation(
    val targetValue: Long,
    val numbers: List<Long>,
) {
    val isValid: Boolean
        get() {
            fun findOperations(index: Int, currentTotal: Long, operations: List<Operator>): List<Operator>? {
                if (index == numbers.size) {
                    // println("attempted ${operations.toEquationString(numbers)} = $currentTotal")
                    return if (currentTotal == targetValue) operations else null
                }

                val addResult = findOperations(
                    index = index + 1,
                    currentTotal = currentTotal + numbers[index],
                    operations = operations + Operator.ADD,
                )
                if (addResult != null) return addResult

                val multiplyResult = findOperations(
                    index = index + 1,
                    currentTotal = currentTotal * numbers[index],
                    operations = operations + Operator.MULTIPLY,
                )
                if (multiplyResult != null) return multiplyResult

                return null
            }

            val operationsToCalculateTarget = findOperations(1, numbers.first(), emptyList())
            return operationsToCalculateTarget != null
        }

    enum class Operator(val symbol: Char) {
        ADD('+'),
        MULTIPLY('x'),
    }
}

private fun List<Operator>.toEquationString(numbers: List<Long>): String {
    val operators = this
    return buildString {
        numbers.forEachIndexed { index, number ->
            append(number)
            operators.getOrNull(index)?.let { operator -> append(" ${operator.symbol} ") }
        }
    }
}