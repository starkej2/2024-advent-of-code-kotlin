import common.Coordinate
import common.Grid
import common.readInput

private const val enableVerboseLogs = false

fun main() {
    val testInput = readInput("Day06_test")
    val realInput = readInput("Day06")

    println("[part 1] TEST result = ${part1(testInput)}")
//    println("[part 2] TEST result = ${part2(testInput)}\n")

    println("[part 1] result = ${part1(realInput)}")
//    println("[part 2] result = ${part2(realInput)}")
}

private fun part1(inputLines: List<String>): Int {
    var map = LabMap(inputLines)
    if (enableVerboseLogs) {
        println("\ninitial map state:")
        map.print()
    }

    val distinctPositionsVisited = buildSet {
        do {
            val nextMove = map.getNextGuardMove()
            map = map.nextState(nextMove)
            if (enableVerboseLogs) {
                println("next state:")
                map.print()
            }
            add(nextMove.oldPosition)
        } while (nextMove !is GuardMove.ExitMap)
    }

    return distinctPositionsVisited.size
}

private fun part2(input: List<String>): Int {
    return -1
}

private data class LabMap(
    val grid: Grid,
) {
    constructor(inputLines: List<String>) : this(Grid(inputLines.map { it.toCharArray() }))

    val guardPosition: GuardPosition? = findGuardPosition()

    fun print() = grid.print()

    private fun findGuardPosition(): GuardPosition? {
        return grid.indexOfFirst(GuardPosition.Orientation.symbols)?.let { position ->
            GuardPosition(
                coordinate = position,
                orientation = GuardPosition.Orientation.fromSymbol(grid.valueAt(position))
            )
        }
    }

    fun getNextGuardMove(): GuardMove {
        if (guardPosition == null) {
            error("Unable to determine next guard position: guard not found")
        }

        val oldPosition = guardPosition.coordinate
        val newPosition = when (guardPosition.orientation) {
            GuardPosition.Orientation.UP -> oldPosition.copy(y = oldPosition.y - 1)
            GuardPosition.Orientation.DOWN -> oldPosition.copy(y = oldPosition.y + 1)
            GuardPosition.Orientation.LEFT -> oldPosition.copy(x = oldPosition.x - 1)
            GuardPosition.Orientation.RIGHT -> oldPosition.copy(x = oldPosition.x + 1)
        }

        return when {
            newPosition.x !in 0 until grid.width || newPosition.y !in 0 until grid.height -> {
                GuardMove.ExitMap(oldPosition, newPosition)
            }

            grid.valueAt(newPosition) == '#' -> {
                GuardMove.TurnRight(oldPosition, newPosition)
            }

            else -> {
                GuardMove.GoForward(oldPosition, newPosition)
            }
        }
    }

    fun nextState(move: GuardMove): LabMap {
        if (guardPosition == null) {
            error("Unable to move guard: guard not found")
        }

        return when (move) {
            is GuardMove.GoForward -> {
                this.copy(
                    grid = grid
                        .setValueAt(guardPosition.coordinate, '.')
                        .setValueAt(move.newPosition, guardPosition.orientation.symbol)
                )
            }

            is GuardMove.TurnRight -> {
                val newOrientation = when (guardPosition.orientation) {
                    GuardPosition.Orientation.UP -> GuardPosition.Orientation.RIGHT
                    GuardPosition.Orientation.DOWN -> GuardPosition.Orientation.LEFT
                    GuardPosition.Orientation.LEFT -> GuardPosition.Orientation.UP
                    GuardPosition.Orientation.RIGHT -> GuardPosition.Orientation.DOWN
                }
                this.copy(
                    grid = grid.setValueAt(guardPosition.coordinate, newOrientation.symbol)
                )
            }

            is GuardMove.ExitMap -> {
                this.copy(
                    grid = grid.setValueAt(guardPosition.coordinate, '.')
                )
            }
        }
    }
}

private data class GuardPosition(
    val coordinate: Coordinate,
    val orientation: Orientation,
) {
    enum class Orientation(val symbol: Char) {
        UP('^'),
        DOWN('v'),
        LEFT('<'),
        RIGHT('>');

        companion object {
            private val orientationBySymbol = mapOf(
                '^' to UP,
                'v' to DOWN,
                '<' to LEFT,
                '>' to RIGHT,
            )
            val symbols: Set<Char> = orientationBySymbol.keys

            fun fromSymbol(symbol: Char): Orientation = orientationBySymbol[symbol]
                ?: error("Invalid orientation symbol: $symbol")
        }
    }
}

sealed interface GuardMove {
    val oldPosition: Coordinate
    val newPosition: Coordinate

    data class GoForward(override val oldPosition: Coordinate, override val newPosition: Coordinate) : GuardMove
    data class TurnRight(override val oldPosition: Coordinate, override val newPosition: Coordinate) : GuardMove
    data class ExitMap(override val oldPosition: Coordinate, override val newPosition: Coordinate) : GuardMove
}
