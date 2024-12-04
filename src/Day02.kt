import kotlin.math.absoluteValue

fun main() {
    val input = readInput("Day02")
    val reports = input.map { line ->
        Report(levels = line.split(" ").map { it.toInt() })
    }

    println("[part 1] ${part1(reports)}")
    println("[part 2] ${part2(reports)}")
}

private fun part1(reports: List<Report>): String {
    return "Safe reports count = ${reports.count { it.isSafe(numErrorsAllowed = 0) }}"
}

private fun part2(reports: List<Report>): String {
    return "Safe reports count = ${reports.count { it.isSafe(numErrorsAllowed = 1) }}"
}

private data class Report(val levels: List<Int>) {

    fun isSafe(numErrorsAllowed: Int): Boolean {
        return isSafe(levels, numErrorsAllowed) ||
                isSafe(levels.asReversed(), numErrorsAllowed)
    }

    companion object {
        private fun isSafe(levels: List<Int>, numErrorsAllowed: Int): Boolean {
            check(levels.size >= 2) { "Report must have at least 2 levels" }

            var leftIndex = 0
            var rightIndex = 1
            var errorCount = 0
            val isFirstStepIncreasing = LevelStep(levels[0], levels[1]).isIncreasing

            while (rightIndex <= levels.lastIndex) {
                val nextStep = LevelStep(startingLevel = levels[leftIndex], nextLevel = levels[rightIndex])
                if (nextStep.isIncreasing == isFirstStepIncreasing && nextStep.isValidSize) {
                    leftIndex = rightIndex
                    rightIndex++
                } else if (errorCount++ < numErrorsAllowed) {
                    rightIndex++
                } else {
                    return false
                }
            }
            return true
        }

        private data class LevelStep(val startingLevel: Int, val nextLevel: Int) {
            private val size: Int = nextLevel - startingLevel
            val isValidSize: Boolean = size.absoluteValue in 1..3
            val isIncreasing: Boolean = size > 0
        }
    }
}
