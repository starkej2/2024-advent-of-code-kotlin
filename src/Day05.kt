fun main() {
    val testInput = readInputAsString("Day05_test")
    val realInput = readInputAsString("Day05")
    println("[part 1] TEST result = ${part1(testInput)}")
    println("[part 2] TEST result = ${part2(testInput)}\n")

    println("[part 1] result = ${part1(realInput)}")
    println("[part 2] result = ${part2(realInput)}")
}

private fun part1(input: String): Int {
    val pageToOrderingRules = loadPageOrderingRules(input)
    val pageUpdates = loadPageUpdates(input)

    return pageUpdates
        .filter { update -> update.isValid(pageToOrderingRules) }
        .sumOf { it.middlePage }
}

private fun part2(input: String): Int {
    return -1
}

private fun loadPageOrderingRules(input: String): Map<PageNumber, PageOrderingRule> {
    val regex = """(\d+)\|(\d+)""".toRegex()
    return regex.findAll(input)
        .map(MatchResult::destructured)
        .groupBy(
            keySelector = { (pageNumber, _) -> pageNumber.toInt() },
            valueTransform = { (_, subsequentPage) -> subsequentPage.toInt() })
        .mapValues { (page, subsequentPages) ->
            PageOrderingRule(page, subsequentPages = subsequentPages.toSet())
        }
}

private fun loadPageUpdates(input: String): List<PageUpdate> {
    return input
        .substringAfter("\n\n")
        .split("\n")
        .map { rawUpdate ->
            PageUpdate(
                affectedPages = rawUpdate
                    .split(",")
                    .map { it.trim().toInt() }
                    .toList()
            )
        }
}

private data class PageOrderingRule(
    val page: PageNumber,
    val subsequentPages: Set<PageNumber>,
)

private data class PageUpdate(
    val affectedPages: List<PageNumber>,
) {
    val middlePage = affectedPages[affectedPages.size / 2]

    fun isValid(rules: Map<PageNumber, PageOrderingRule>): Boolean {
        affectedPages.forEachIndexed { index, page ->
            rules[page]?.let { pageRule ->
                val priorPages = affectedPages.take(index)
                if (priorPages.any { it in pageRule.subsequentPages }) {
                    return false
                }
            }
        }
        return true
    }
}

private typealias PageNumber = Int