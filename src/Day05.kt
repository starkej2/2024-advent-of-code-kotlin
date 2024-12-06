import common.readInputAsString

fun main() {
    val testInput = readInputAsString("Day05_test")
    val realInput = readInputAsString("Day05")
    println("[part 1] TEST result = ${part1(testInput)}")
    println("[part 2] TEST result = ${part2(testInput)}\n")

    println("[part 1] result = ${part1(realInput)}")
    println("[part 2] result = ${part2(realInput)}")
}

private fun part1(input: String): Int {
    val rulesByPage = loadPageOrderingRules(input)
    val pageUpdates = loadPageUpdates(input)

    return pageUpdates
        .filter { update -> update.isValid(rulesByPage) }
        .sumOf { it.middlePage }
}

private fun part2(input: String): Int {
    val rulesByPage = loadPageOrderingRules(input)
    val pageUpdates = loadPageUpdates(input)
    return pageUpdates
        .filterNot { update -> update.isValid(rulesByPage) }
        .map { invalidUpdate -> invalidUpdate.fix(rulesByPage) }
        .sumOf { it.middlePage }
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

private fun PageUpdate.fix(
    rulesByPage: Map<PageNumber, PageOrderingRule>,
): PageUpdate {
    val comparator = Comparator<PageNumber> { page1, page2 ->
        val page1GoesBefore2 = rulesByPage[page1]?.subsequentPages?.contains(page2) ?: false
        val page2GoesBefore1 = rulesByPage[page2]?.subsequentPages?.contains(page1) ?: false

        when {
            page1GoesBefore2 -> -1
            page2GoesBefore1 -> 1
            else -> 0
        }
    }
    return this.copy(affectedPages = affectedPages.sortedWith(comparator))
}

private data class PageOrderingRule(
    val page: PageNumber,
    val subsequentPages: Set<PageNumber>,
)

private data class PageUpdate(
    val affectedPages: List<PageNumber>,
) {
    val middlePage = affectedPages[affectedPages.size / 2]

    fun isValid(rulesByPage: Map<PageNumber, PageOrderingRule>): Boolean {
        affectedPages.forEachIndexed { index, page ->
            rulesByPage[page]?.let { pageRule ->
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