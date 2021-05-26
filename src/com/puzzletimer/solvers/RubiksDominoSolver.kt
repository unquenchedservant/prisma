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

object RubiksDominoSolver {
    const val N_CORNERS_PERMUTATIONS = 40320
    const val N_EDGES_PERMUTATIONS = 40320
    const val N_MOVES = 10
    private var moves: Array<State>
    private var faces: IntArray
    private var cornersPermutationMove: Array<IntArray>
    private var edgesPermutationMove: Array<IntArray>
    private var cornersPermutationDistance: ByteArray
    private var edgesPermutationDistance: ByteArray
    fun solve(state: State): Array<String?> {
        val cornersPermutation = IndexMapping.permutationToIndex(state.cornersPermutation)
        val edgesPermutation = IndexMapping.permutationToIndex(state.edgesPermutation)
        var depth = 0
        while (true) {
            val solution = ArrayList<Int>()
            if (search(cornersPermutation, edgesPermutation, depth, solution, -1)) {
                val moveNames = arrayOf("U", "U2", "U'", "D", "D2", "D'", "L", "R", "F", "B")
                val sequence = arrayOfNulls<String>(solution.size)
                for (i in sequence.indices) {
                    sequence[i] = moveNames[solution[i]]
                }
                return sequence
            }
            depth++
        }
    }

    private fun search(cornersPermutation: Int, edgesPermutation: Int, depth: Int, solution: ArrayList<Int>, lastFace: Int): Boolean {
        if (depth == 0) {
            return cornersPermutation == 0 && edgesPermutation == 0
        }
        if (cornersPermutationDistance[cornersPermutation] <= depth &&
                edgesPermutationDistance[edgesPermutation] <= depth) {
            for (i in 0 until N_MOVES) {
                if (faces[i] == lastFace) {
                    continue
                }
                solution.add(i)
                if (search(
                                cornersPermutationMove[cornersPermutation][i],
                                edgesPermutationMove[edgesPermutation][i],
                                depth - 1,
                                solution,
                                faces[i])) {
                    return true
                }
                solution.removeAt(solution.size - 1)
            }
        }
        return false
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
                        random.nextInt(N_EDGES_PERMUTATIONS), 8))
    }

    class State(var cornersPermutation: ByteArray?, var edgesPermutation: ByteArray?) {
        fun multiply(move: State): State {
            val cornersPermutation = ByteArray(8)
            val edgesPermutation = ByteArray(8)
            for (i in 0..7) {
                cornersPermutation[i] = this.cornersPermutation!![move.cornersPermutation!![i].toInt()]
                edgesPermutation[i] = this.edgesPermutation!![move.edgesPermutation!![i].toInt()]
            }
            return State(cornersPermutation, edgesPermutation)
        }
    }

    init {
        val moveU = State(byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7))
        val moveD = State(byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4))
        val moveL = State(byteArrayOf(7, 1, 2, 4, 3, 5, 6, 0), byteArrayOf(0, 1, 2, 7, 4, 5, 6, 3))
        val moveR = State(byteArrayOf(0, 6, 5, 3, 4, 2, 1, 7), byteArrayOf(0, 5, 2, 3, 4, 1, 6, 7))
        val moveF = State(byteArrayOf(0, 1, 7, 6, 4, 5, 3, 2), byteArrayOf(0, 1, 6, 3, 4, 5, 2, 7))
        val moveB = State(byteArrayOf(5, 4, 2, 3, 1, 0, 6, 7), byteArrayOf(4, 1, 2, 3, 0, 5, 6, 7))
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
        faces = intArrayOf(
                0, 0, 0,
                1, 1, 1,
                2,
                3,
                4,
                5)

        // move tables
        cornersPermutationMove = Array(N_CORNERS_PERMUTATIONS) { IntArray(N_MOVES) }
        for (i in 0 until N_CORNERS_PERMUTATIONS) {
            val state = State(IndexMapping.indexToPermutation(i, 8), ByteArray(8))
            for (j in 0 until N_MOVES) {
                cornersPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.state.multiply(moves[j]).cornersPermutation)
            }
        }
        edgesPermutationMove = Array(N_EDGES_PERMUTATIONS) { IntArray(N_MOVES) }
        for (i in 0 until N_EDGES_PERMUTATIONS) {
            val state = State(ByteArray(8), IndexMapping.indexToPermutation(i, 8))
            for (j in 0 until N_MOVES) {
                edgesPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.state.multiply(moves[j]).edgesPermutation)
            }
        }

        // prune tables
        cornersPermutationDistance = ByteArray(N_CORNERS_PERMUTATIONS)
        for (i in cornersPermutationDistance.indices) {
            cornersPermutationDistance[i] = -1
        }
        cornersPermutationDistance[0] = 0
        val distance = 0
        val nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_CORNERS_PERMUTATIONS) {
            for (i in 0 until N_CORNERS_PERMUTATIONS) {
                if (cornersPermutationDistance[i] == com.puzzletimer.solvers.distance) {
                    for (k in 0 until N_MOVES) {
                        val next = cornersPermutationMove[i][k]
                        if (cornersPermutationDistance[com.puzzletimer.solvers.next] < 0) {
                            cornersPermutationDistance[com.puzzletimer.solvers.next] = (com.puzzletimer.solvers.distance + 1) as Byte
                            com.puzzletimer.solvers.nVisited++
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }
        edgesPermutationDistance = ByteArray(N_CORNERS_PERMUTATIONS)
        for (i in edgesPermutationDistance.indices) {
            edgesPermutationDistance[i] = -1
        }
        edgesPermutationDistance[0] = 0
        com.puzzletimer.solvers.distance = 0
        com.puzzletimer.solvers.nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_CORNERS_PERMUTATIONS) {
            for (i in 0 until N_CORNERS_PERMUTATIONS) {
                if (edgesPermutationDistance[i] == com.puzzletimer.solvers.distance) {
                    for (k in 0 until N_MOVES) {
                        val next = edgesPermutationMove[i][k]
                        if (edgesPermutationDistance[com.puzzletimer.solvers.next] < 0) {
                            edgesPermutationDistance[com.puzzletimer.solvers.next] = (com.puzzletimer.solvers.distance + 1) as Byte
                            com.puzzletimer.solvers.nVisited++
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }
    }
}