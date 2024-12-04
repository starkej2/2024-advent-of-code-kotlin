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
    return "Safe reports count = ${reports.count { it.isSafe(enableProblemDampener = false) }}"
}

private fun part2(reports: List<Report>): String {
    return "Safe reports count = ${reports.count { it.isSafe(enableProblemDampener = true) }}"
}

private data class Report(val levels: List<Int>) {

    fun isSafe(enableProblemDampener: Boolean): Boolean {
        if (countErrors(levels) == 0) {
            return true
        }

        // Try removing each level and check
        if (enableProblemDampener) {
            for (i in levels.indices) {
                val modifiedLevels = levels.toMutableList().apply { removeAt(i) }
                if (countErrors(modifiedLevels) == 0) {
                    return true
                }
            }
        }

        return false
    }

    companion object {
        private const val MIN_SAFE_LEVEL_STEP_SIZE = 1
        private const val MAX_SAFE_LEVEL_STEP_SIZE = 3

        private fun countErrors(levels: List<Int>): Int {
            var errorCount = 0
            var previousDirection: StepDirection? = null

            for (i in 0..<levels.size - 1) {
                val a = levels[i]
                val b = levels[i + 1]
                val stepSize = b - a

                val nextDirection: StepDirection? = when {
                    stepSize > 0 -> StepDirection.INCREASING
                    stepSize < 0 -> StepDirection.DECREASING
                    else -> previousDirection
                }

                val isValidStepSize = stepSize.absoluteValue in MIN_SAFE_LEVEL_STEP_SIZE..MAX_SAFE_LEVEL_STEP_SIZE
                val isValidDirection = previousDirection == null || (nextDirection == previousDirection)
                if (!isValidStepSize || !isValidDirection) {
                    errorCount++
                }

                previousDirection = nextDirection
            }
            return errorCount
        }

        private enum class StepDirection {
            INCREASING,
            DECREASING,
        }
    }
}
