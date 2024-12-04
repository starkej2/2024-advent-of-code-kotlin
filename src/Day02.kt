import kotlin.math.abs

fun main() {
    val input = readInput("Day02")
    val reports = input.map { Report.parseLine(it) }

    println("[part 1] ${part1(reports)}")
    println("[part 2] ${part2(reports)}")
}

private fun part1(reports: List<Report>): String {
    return "Safe reports count = ${reports.count { it.isSafe }}"
}

private fun part2(reports: List<Report>): String {
    return "Safe reports count = ${reports.count { it.isSafe }}"
}

private data class Report(val levels: List<Int>) {
    val isSafe: Boolean
        get() {
            var currentDirection: StepDirection? = null

            for (i in 0..<levels.size - 1) {
                val a = levels[i]
                val b = levels[i + 1]
                val stepSize = b - a

                val nextDirection: StepDirection = when (stepSize) {
                    in MIN_SAFE_LEVEL_STEP_SIZE..MAX_SAFE_LEVEL_STEP_SIZE -> {
                        StepDirection.INCREASING
                    }

                    in -MAX_SAFE_LEVEL_STEP_SIZE..-MIN_SAFE_LEVEL_STEP_SIZE -> {
                        StepDirection.DECREASING
                    }

                    else -> {
                        // step size is out of range
                        return false
                    }
                }

                if (currentDirection == null) {
                    currentDirection = nextDirection
                } else if (currentDirection != nextDirection) {
                    // direction changes are not allowed
                    return false
                }
            }

            return true
        }

    companion object {
        const val MIN_SAFE_LEVEL_STEP_SIZE = 1
        const val MAX_SAFE_LEVEL_STEP_SIZE = 3

        fun parseLine(line: String): Report = Report(
            levels = line.split(" ").map { it.toInt() }
        )
    }

    private enum class StepDirection {
        INCREASING,
        DECREASING,
    }
}
