fun main() {
    val input = readInputAsString("Day03")
    println("[part 1] result = ${part1(input)}")
    println("[part 2] result = ${part2(input)}")
}

private fun part1(corruptedMemory: String): Int {
    return MultiplyOperation.extractFrom(corruptedMemory)
        .sumOf { it.product }
}

private fun part2(corruptedMemory: String): Int {
    val instructions = buildList {
        var chunkStartIndex = 0

        while (chunkStartIndex in 0 until corruptedMemory.lastIndex) {
            val chunkEndIndex = corruptedMemory
                .indexOf("don't()", chunkStartIndex)
                .takeIf { it > 0 } ?: corruptedMemory.lastIndex

            val chunkText = corruptedMemory.substring(chunkStartIndex, chunkEndIndex)
            addAll(MultiplyOperation.extractFrom(chunkText))

            chunkStartIndex = corruptedMemory
                .indexOf("do()", chunkEndIndex)
                .takeIf { it > 0 } ?: break
        }
    }

    return instructions.sumOf { it.product }
}

data class MultiplyOperation(val factor1: Int, val factor2: Int) {
    val product = factor1 * factor2

    companion object {
        private val REGEX = """mul\((?<factor1>\d{1,3}),(?<factor2>\d{1,3})\)"""
            .toRegex(RegexOption.IGNORE_CASE)

        fun extractFrom(string: String): List<MultiplyOperation> {
            return REGEX.findAll(string)
                .map {
                    MultiplyOperation(
                        factor1 = it.groups["factor1"]!!.value.toInt(),
                        factor2 = it.groups["factor2"]!!.value.toInt()
                    )
                }
                .toList()
        }
    }
}
