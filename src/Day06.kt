import GuardPosition.Direction
import common.Coordinate
import common.Grid
import common.readInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    val testInput = readInput("Day06_test")
    val realInput = readInput("Day06")

    println("[part 1] TEST result = ${part1(testInput)}")
    println("[part 2] TEST result = ${part2(testInput)}\n")

    println("[part 1] result = ${part1(realInput)}")
    println("[part 2] result = ${part2(realInput)}")
}

private fun part1(inputLines: List<String>): Int {
    return getPositionsVisitedByGuard(LabMap(inputLines)).distinctBy { it.coordinate }.size
}

private fun part2(inputLines: List<String>): Int {
    val originalMap = LabMap(inputLines)
    val originalPath = getPositionsVisitedByGuard(originalMap)
    val loopCounter = AtomicInteger(0)

    runBlocking(Dispatchers.Default) {
        loop@ for (coordinate in originalPath.map { it.coordinate }.distinct()) {
            launch {
                try {
                    val loopTestMap = originalMap.copy(newObstacleLocation = coordinate)
                    val visited = mutableSetOf<GuardPosition>()
                    runGuard(
                        initialMap = loopTestMap,
                        onNewPositionVisited = { newPosition ->
                            if (newPosition in visited) {
                                loopCounter.andIncrement
                                throw Exception("loop detected")
                            }
                            visited.add(newPosition)
                        },
                    )
                } catch (e: Exception) {
                    // hack to exit loop
                    // println("loop detected at $coordinate")
                }
            }
        }
    }

    return loopCounter.get()
}

private fun runGuard(
    initialMap: LabMap,
    onNewPositionVisited: (GuardPosition) -> Unit,
) {
    var map = initialMap
    if (initialMap.guardPosition == null) {
        // println("\nno guard found in this map:")
        // initialMap.print()
        return
    }
    onNewPositionVisited(initialMap.guardPosition)

    do {
        val nextMove = map.getNextGuardMove()
        map = map.nextState(nextMove)

        if (nextMove !is GuardMove.ExitMap) {
            onNewPositionVisited(nextMove.newPosition)
        }
    } while (nextMove !is GuardMove.ExitMap)
}

private fun getPositionsVisitedByGuard(
    initialMap: LabMap,
): Set<GuardPosition> {
    return buildSet {
        runGuard(
            initialMap = initialMap,
            onNewPositionVisited = { position -> add(position) },
        )
    }
}

private data class LabMap(
    val grid: Grid,
) {
    constructor(inputLines: List<String>) : this(Grid(inputLines.map { it.toCharArray() }))

    val guardPosition: GuardPosition? = findGuardPosition()

    fun print() = grid.print()

    private fun findGuardPosition(): GuardPosition? {
        return grid.indexOfFirst(Direction.symbols)?.let { position ->
            GuardPosition(
                coordinate = position,
                direction = Direction.fromSymbol(grid.valueAt(position))
            )
        }
    }

    fun getNextGuardMove(): GuardMove {
        if (guardPosition == null) {
            error("Unable to determine next guard position: guard not found")
        }

        val oldPosition = guardPosition
        val newCoordinate = Coordinate(
            x = oldPosition.coordinate.x + guardPosition.direction.xDelta,
            y = oldPosition.coordinate.y + guardPosition.direction.yDelta
        )

        return when {
            newCoordinate.x !in 0 until grid.width || newCoordinate.y !in 0 until grid.height -> {
                GuardMove.ExitMap(
                    oldPosition = oldPosition,
                    newPosition = oldPosition.copy(coordinate = newCoordinate),
                )
            }

            grid.valueAt(newCoordinate) == '#' -> {
                val newDirection = when (guardPosition.direction) {
                    Direction.UP -> Direction.RIGHT
                    Direction.DOWN -> Direction.LEFT
                    Direction.LEFT -> Direction.UP
                    Direction.RIGHT -> Direction.DOWN
                }

                GuardMove.TurnRight(
                    oldPosition = oldPosition,
                    newPosition = GuardPosition(coordinate = oldPosition.coordinate, direction = newDirection),
                )
            }

            else -> {
                GuardMove.GoForward(
                    oldPosition = oldPosition,
                    newPosition = oldPosition.copy(coordinate = newCoordinate),
                )
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
                        .setValueAt(move.newPosition.coordinate, move.newPosition.direction.symbol)
                )
            }

            is GuardMove.TurnRight -> {
                this.copy(
                    grid = grid
                        .setValueAt(guardPosition.coordinate, '.')
                        .setValueAt(move.newPosition.coordinate, move.newPosition.direction.symbol)
                )
            }

            is GuardMove.ExitMap -> {
                this.copy(
                    grid = grid.setValueAt(guardPosition.coordinate, '.')
                )
            }
        }
    }

    fun copy(newObstacleLocation: Coordinate): LabMap = this.copy(
        grid = grid.setValueAt(newObstacleLocation, '#')
    )
}

private data class GuardPosition(
    val coordinate: Coordinate,
    val direction: Direction,
) {
    enum class Direction(
        val symbol: Char,
        val xDelta: Int,
        val yDelta: Int,
    ) {
        UP(
            symbol = '^',
            xDelta = 0,
            yDelta = -1,
        ),
        DOWN(
            symbol = 'v',
            xDelta = 0,
            yDelta = 1,
        ),
        LEFT(
            symbol = '<',
            xDelta = -1,
            yDelta = 0,
        ),
        RIGHT(
            symbol = '>',
            xDelta = 1,
            yDelta = 0,
        );

        companion object {
            private val orientationBySymbol = mapOf(
                '^' to UP,
                'v' to DOWN,
                '<' to LEFT,
                '>' to RIGHT,
            )
            val symbols: Set<Char> = orientationBySymbol.keys

            fun fromSymbol(symbol: Char): Direction = orientationBySymbol[symbol]
                ?: error("Invalid orientation symbol: $symbol")
        }
    }
}

private sealed interface GuardMove {
    val oldPosition: GuardPosition
    val newPosition: GuardPosition

    data class GoForward(override val oldPosition: GuardPosition, override val newPosition: GuardPosition) : GuardMove
    data class TurnRight(override val oldPosition: GuardPosition, override val newPosition: GuardPosition) : GuardMove
    data class ExitMap(override val oldPosition: GuardPosition, override val newPosition: GuardPosition) : GuardMove
}
