package com.puzzletimer.solvers

import com.puzzletimer.solvers.FloppyCubeSolver
import com.puzzletimer.solvers.IndexMapping
import com.puzzletimer.solvers.RubiksClockSolver
import com.puzzletimer.solvers.RubiksCubeSolver
import com.puzzletimer.solvers.RubiksCubeCrossSolver
import com.puzzletimer.solvers.RubiksCubeRUSolver
import com.puzzletimer.solvers.RubiksCubeXCrossSolver
import com.puzzletimer.solvers.RubiksDominoSolver
import com.puzzletimer.solvers.Square1ShapeSolver
import com.puzzletimer.solvers.Square1Solver.CubeState
import com.puzzletimer.solvers.TowerCubeSolver
import java.util.*

object TowerCubeSolver {
    private const val N_CORNERS_PERMUTATIONS = 40320
    private const val N_EDGES_PERMUTATIONS = 24
    private const val N_MOVES = 10
    private var moves: Array<State>
    private var cornersPermutationMove: Array<IntArray>
    private var edgesPermutationMove: Array<IntArray>
    private var distance: Array<IntArray>
    fun solve(state: State): Array<String?> {
        val moveNames = arrayOf("U", "U2", "U'", "D", "D2", "D'", "L", "R", "F", "B")
        val sequence = ArrayList<String>()
        var cornersPermutationIndex = IndexMapping.permutationToIndex(state.cornersPermutation)
        var edgesPermutationIndex = IndexMapping.permutationToIndex(state.edgesPermutation)
        while (true) {
            if (distance[cornersPermutationIndex][edgesPermutationIndex] == 0) {
                break
            }
            for (k in 0 until N_MOVES) {
                val nextCornersPermutationIndex = cornersPermutationMove[cornersPermutationIndex][k]
                val nextEdgesPermutationIndex = edgesPermutationMove[edgesPermutationIndex][k]
                if (distance[nextCornersPermutationIndex][nextEdgesPermutationIndex] ==
                        distance[cornersPermutationIndex][edgesPermutationIndex] - 1) {
                    sequence.add(moveNames[k])
                    cornersPermutationIndex = nextCornersPermutationIndex
                    edgesPermutationIndex = nextEdgesPermutationIndex
                    break
                }
            }
        }
        val sequenceArray = arrayOfNulls<String>(sequence.size)
        sequence.toArray(sequenceArray)
        return sequenceArray
    }

    fun generate(state: State): Array<String?> {
        val solution = solve(state)
        val inverseMoves = HashMap<String?, String>()
        inverseMoves["U"] = "U'"
        inverseMoves["U2"] = "U2"
        inverseMoves["U'"] = "U"
        inverseMoves["D"] = "D'"
        inverseMoves["D2"] = "D2"
        inverseMoves["D'"] = "D"
        inverseMoves["L"] = "L"
        inverseMoves["R"] = "R"
        inverseMoves["F"] = "F"
        inverseMoves["B"] = "B"
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in sequence.indices) {
            sequence[i] = inverseMoves[solution[solution.size - 1 - i]]
        }
        return sequence
    }

    fun getRandomState(random: Random): State {
        return State(
                IndexMapping.indexToPermutation(
                        random.nextInt(N_CORNERS_PERMUTATIONS), 8),
                IndexMapping.indexToPermutation(
                        random.nextInt(N_EDGES_PERMUTATIONS), 4))
    }

    class State(var cornersPermutation: ByteArray?, var edgesPermutation: ByteArray?) {
        fun multiply(move: State): State {
            // corners
            val cornersPermutation = ByteArray(8)
            for (i in 0..7) {
                cornersPermutation[i] = this.cornersPermutation!![move.cornersPermutation!![i].toInt()]
            }

            // edges
            val edgesPermutation = ByteArray(4)
            for (i in 0..3) {
                edgesPermutation[i] = this.edgesPermutation!![move.edgesPermutation!![i].toInt()]
            }
            return State(cornersPermutation, edgesPermutation)
        }
    }

    init {
        val moveU = State(byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(0, 1, 2, 3))
        val moveD = State(byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 1, 2, 3))
        val moveL = State(byteArrayOf(7, 1, 2, 4, 3, 5, 6, 0), byteArrayOf(3, 1, 2, 0))
        val moveR = State(byteArrayOf(0, 6, 5, 3, 4, 2, 1, 7), byteArrayOf(0, 2, 1, 3))
        val moveF = State(byteArrayOf(0, 1, 7, 6, 4, 5, 3, 2), byteArrayOf(0, 1, 3, 2))
        val moveB = State(byteArrayOf(5, 4, 2, 3, 1, 0, 6, 7), byteArrayOf(1, 0, 2, 3))
        moves = arrayOf(
                com.puzzletimer.solvers.moveU,
                com.puzzletimer.solvers.moveU.multiply(com.puzzletimer.solvers.moveU),
                com.puzzletimer.solvers.moveU.multiply(com.puzzletimer.solvers.moveU).multiply(com.puzzletimer.solvers.moveU),
                com.puzzletimer.solvers.moveD,
                com.puzzletimer.solvers.moveD.multiply(com.puzzletimer.solvers.moveD),
                com.puzzletimer.solvers.moveD.multiply(com.puzzletimer.solvers.moveD).multiply(com.puzzletimer.solvers.moveD),
                com.puzzletimer.solvers.moveL,
                com.puzzletimer.solvers.moveR,
                com.puzzletimer.solvers.moveF,
                com.puzzletimer.solvers.moveB)

        // move tables
        cornersPermutationMove = Array(N_CORNERS_PERMUTATIONS) { IntArray(N_MOVES) }
        for (i in 0 until N_CORNERS_PERMUTATIONS) {
            val state = State(IndexMapping.indexToPermutation(i, 8), ByteArray(4))
            for (j in 0 until N_MOVES) {
                cornersPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.state.multiply(moves[j]).cornersPermutation)
            }
        }
        edgesPermutationMove = Array(N_EDGES_PERMUTATIONS) { IntArray(N_MOVES) }
        for (i in 0 until N_EDGES_PERMUTATIONS) {
            val state = State(ByteArray(8), IndexMapping.indexToPermutation(i, 4))
            for (j in 0 until N_MOVES) {
                edgesPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.state.multiply(moves[j]).edgesPermutation)
            }
        }

        // distance table
        distance = Array(N_CORNERS_PERMUTATIONS) { IntArray(N_EDGES_PERMUTATIONS) }
        for (i in distance.indices) {
            for (j in 0 until distance[i].length) {
                distance[i][j] = -1
            }
        }
        distance[0][0] = 0
        var nVisited: Int
        val depth = 0
        do {
            com.puzzletimer.solvers.nVisited = 0
            for (i in 0 until N_CORNERS_PERMUTATIONS) {
                for (j in 0 until N_EDGES_PERMUTATIONS) {
                    if (distance[i][j] == com.puzzletimer.solvers.depth) {
                        for (k in 0 until N_MOVES) {
                            val nextCornersPemutation = cornersPermutationMove[i][k]
                            val nextEdgesPemutation = edgesPermutationMove[j][k]
                            if (distance[com.puzzletimer.solvers.nextCornersPemutation][com.puzzletimer.solvers.nextEdgesPemutation] == -1) {
                                distance[com.puzzletimer.solvers.nextCornersPemutation][com.puzzletimer.solvers.nextEdgesPemutation] = com.puzzletimer.solvers.depth + 1
                                com.puzzletimer.solvers.nVisited++
                            }
                        }
                    }
                }
            }
            com.puzzletimer.solvers.depth++
        } while (com.puzzletimer.solvers.nVisited > 0)
    }
}