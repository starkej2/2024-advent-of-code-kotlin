import kotlin.math.abs

fun main() {
//    val input = readInput("Test")
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
            val stepSizes = levels
                .windowed(2)
                .map { (a, b) -> b - a }

            val containsInvalidStepSize = stepSizes
                .map { abs(it) }
                .any { it < MIN_SAFE_LEVEL_STEP_SIZE || it > MAX_SAFE_LEVEL_STEP_SIZE }
            if (containsInvalidStepSize) {
                return false
            }

            // check if steps are in the same direction
            stepSizes.windowed(2).forEach { (a, b) ->
                if ((a > 0 && b < 0) || a < 0 && b > 0) {
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
}