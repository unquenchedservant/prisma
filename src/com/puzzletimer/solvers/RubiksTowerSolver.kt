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

class RubiksTowerSolver {
    class State(var orientation: ByteArray?, var edgesPermutation: ByteArray?, var cornersPermutation: ByteArray?) {
        fun multiply(move: State): State {
            val orientation = ByteArray(8)
            val edgesPermutation = ByteArray(8)
            val cornersPermutation = ByteArray(8)
            for (i in 0..7) {
                orientation[i] = ((this.orientation!![move.edgesPermutation!![i].toInt()] + move.orientation!![i]) % 3).toByte()
                edgesPermutation[i] = this.edgesPermutation!![move.edgesPermutation!![i].toInt()]
                cornersPermutation[i] = this.cornersPermutation!![move.cornersPermutation!![i].toInt()]
            }
            return State(orientation, edgesPermutation, cornersPermutation)
        }
    }

    private val N_ORIENTATIONS = 2187
    private val N_EDGES_PERMUTATIONS = 40320
    private val N_EDGES_COMBINATIONS = 70
    private val N_CORNERS_PERMUTATIONS = 40320
    private val N_CORNERS_COMBINATIONS = 70
    private var initialized = false
    private var moves1: Array<State>
    private var moveNames1: Array<String>
    private var faces1: IntArray
    private var moves2: Array<State>
    private var moveNames2: Array<String>
    private var faces2: IntArray
    private var orientationMove: Array<IntArray>
    private var edgesPermutationMove: Array<IntArray>
    private var edgesCombinationMove: Array<IntArray>
    private var cornersPermutationMove: Array<IntArray>
    private var cornersCombinationMove: Array<IntArray>
    private var orientationDistance: ByteArray
    private var edgesPermutationDistance: Array<ByteArray>
    private var cornersPermutationDistance: Array<ByteArray>
    private fun initialize() {
        // moves
        val moveUw = State(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7))
        val moveDw = State(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4))
        val moveLw = State(byteArrayOf(2, 0, 0, 1, 1, 0, 0, 2), byteArrayOf(4, 1, 2, 0, 7, 5, 6, 3), byteArrayOf(4, 1, 2, 0, 7, 5, 6, 3))
        val moveRw = State(byteArrayOf(0, 1, 2, 0, 0, 2, 1, 0), byteArrayOf(0, 2, 6, 3, 4, 1, 5, 7), byteArrayOf(0, 2, 6, 3, 4, 1, 5, 7))
        val moveFw = State(byteArrayOf(0, 0, 1, 2, 0, 0, 2, 1), byteArrayOf(0, 1, 3, 7, 4, 5, 2, 6), byteArrayOf(0, 1, 3, 7, 4, 5, 2, 6))
        val moveBw = State(byteArrayOf(1, 2, 0, 0, 2, 1, 0, 0), byteArrayOf(1, 5, 2, 3, 0, 4, 6, 7), byteArrayOf(1, 5, 2, 3, 0, 4, 6, 7))
        val moveU = State(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7))
        val moveD = State(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4))
        moves1 = arrayOf(
                moveUw,
                moveUw.multiply(moveUw),
                moveUw.multiply(moveUw).multiply(moveUw),
                moveDw,
                moveDw.multiply(moveDw),
                moveDw.multiply(moveDw).multiply(moveDw),
                moveLw,
                moveLw.multiply(moveLw),
                moveLw.multiply(moveLw).multiply(moveLw),
                moveRw,
                moveRw.multiply(moveRw),
                moveRw.multiply(moveRw).multiply(moveRw),
                moveFw,
                moveFw.multiply(moveFw),
                moveFw.multiply(moveFw).multiply(moveFw),
                moveBw,
                moveBw.multiply(moveBw),
                moveBw.multiply(moveBw).multiply(moveBw))
        moveNames1 = arrayOf(
                "Uw", "Uw2", "Uw'",
                "Dw", "Dw2", "Dw'",
                "Lw", "Lw2", "Lw'",
                "Rw", "Rw2", "Rw'",
                "Fw", "Fw2", "Fw'",
                "Bw", "Bw2", "Bw'")
        faces1 = intArrayOf(
                0, 0, 0,
                1, 1, 1,
                2, 2, 2,
                3, 3, 3,
                4, 4, 4,
                5, 5, 5)
        moves2 = arrayOf(
                moveUw,
                moveUw.multiply(moveUw),
                moveUw.multiply(moveUw).multiply(moveUw),
                moveDw,
                moveDw.multiply(moveDw),
                moveDw.multiply(moveDw).multiply(moveDw),
                moveLw.multiply(moveLw),
                moveRw.multiply(moveRw),
                moveFw.multiply(moveFw),
                moveBw.multiply(moveBw),
                moveU,
                moveU.multiply(moveU),
                moveU.multiply(moveU).multiply(moveU),
                moveD,
                moveD.multiply(moveD),
                moveD.multiply(moveD).multiply(moveD))
        moveNames2 = arrayOf(
                "Uw", "Uw2", "Uw'",
                "Dw", "Dw2", "Dw'",
                "Lw2",
                "Rw2",
                "Fw2",
                "Bw2",
                "U", "U2", "U'",
                "D", "D2", "D'")
        faces2 = intArrayOf(
                0, 0, 0,
                1, 1, 1,
                2,
                3,
                4,
                5,
                6, 6, 6,
                7, 7, 7)

        // move tables
        orientationMove = Array(N_ORIENTATIONS) { IntArray(moves1.size) }
        for (i in orientationMove.indices) {
            val state = State(IndexMapping.indexToZeroSumOrientation(i, 3, 8), ByteArray(8), ByteArray(8))
            for (j in moves1.indices) {
                orientationMove[i][j] = IndexMapping.zeroSumOrientationToIndex(
                        state.multiply(moves1[j]).orientation, 3)
            }
        }
        edgesPermutationMove = Array(N_EDGES_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in edgesPermutationMove.indices) {
            val state = State(ByteArray(8), IndexMapping.indexToPermutation(i, 8), ByteArray(8))
            for (j in moves2.indices) {
                edgesPermutationMove[i][j] = IndexMapping.permutationToIndex(
                        state.multiply(moves2[j]).edgesPermutation)
            }
        }
        edgesCombinationMove = Array(N_EDGES_COMBINATIONS) { IntArray(moves2.size) }
        for (i in edgesCombinationMove.indices) {
            val combination = IndexMapping.indexToCombination(i, 4, 8)
            val edges = ByteArray(8)
            var nextTop: Byte = 0
            var nextBottom: Byte = 4
            for (j in edges.indices) {
                if (combination!![j]) {
                    edges[j] = nextTop++
                } else {
                    edges[j] = nextBottom++
                }
            }
            val state = State(ByteArray(8), edges, ByteArray(8))
            for (j in moves2.indices) {
                val result = state.multiply(moves2[j])
                val isTopEdge = BooleanArray(8)
                for (k in isTopEdge.indices) {
                    isTopEdge[k] = result.edgesPermutation!![k] < 4
                }
                edgesCombinationMove[i][j] = IndexMapping.combinationToIndex(isTopEdge, 4)
            }
        }
        cornersPermutationMove = Array(N_CORNERS_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in cornersPermutationMove.indices) {
            val state = State(ByteArray(8), ByteArray(8), IndexMapping.indexToPermutation(i, 8))
            for (j in moves2.indices) {
                cornersPermutationMove[i][j] = IndexMapping.permutationToIndex(
                        state.multiply(moves2[j]).cornersPermutation)
            }
        }
        cornersCombinationMove = Array(N_CORNERS_COMBINATIONS) { IntArray(moves2.size) }
        for (i in cornersCombinationMove.indices) {
            val combination = IndexMapping.indexToCombination(i, 4, 8)
            val corners = ByteArray(8)
            var nextTop: Byte = 0
            var nextBottom: Byte = 4
            for (j in corners.indices) {
                if (combination!![j]) {
                    corners[j] = nextTop++
                } else {
                    corners[j] = nextBottom++
                }
            }
            val state = State(ByteArray(8), ByteArray(8), corners)
            for (j in moves2.indices) {
                val result = state.multiply(moves2[j])
                val isTopCorner = BooleanArray(8)
                for (k in isTopCorner.indices) {
                    isTopCorner[k] = result.cornersPermutation!![k] < 4
                }
                cornersCombinationMove[i][j] = IndexMapping.combinationToIndex(isTopCorner, 4)
            }
        }

        // prune tables
        orientationDistance = ByteArray(N_ORIENTATIONS)
        for (i in orientationDistance.indices) {
            orientationDistance[i] = -1
        }
        orientationDistance[0] = 0
        var depth = 0
        var nVisited: Int
        do {
            nVisited = 0
            for (i in orientationDistance.indices) {
                if (orientationDistance[i] == depth) {
                    for (j in moves1.indices) {
                        val next = orientationMove[i][j]
                        if (orientationDistance[next] < 0) {
                            orientationDistance[next] = (depth + 1).toByte()
                            nVisited++
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        edgesPermutationDistance = Array(N_EDGES_PERMUTATIONS) { ByteArray(N_CORNERS_COMBINATIONS) }
        for (i in edgesPermutationDistance.indices) {
            for (j in 0 until edgesPermutationDistance[i].length) {
                edgesPermutationDistance[i][j] = -1
            }
        }
        edgesPermutationDistance[0][0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in edgesPermutationDistance.indices) {
                for (j in 0 until edgesPermutationDistance[i].length) {
                    if (edgesPermutationDistance[i][j] == depth) {
                        for (k in moves2.indices) {
                            val nextPermutation = edgesPermutationMove[i][k]
                            val nextCombination = cornersCombinationMove[j][k]
                            if (edgesPermutationDistance[nextPermutation][nextCombination] < 0) {
                                edgesPermutationDistance[nextPermutation][nextCombination] = (depth + 1).toByte()
                                nVisited++
                            }
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        cornersPermutationDistance = Array(N_CORNERS_PERMUTATIONS) { ByteArray(N_EDGES_COMBINATIONS) }
        for (i in cornersPermutationDistance.indices) {
            for (j in 0 until cornersPermutationDistance[i].length) {
                cornersPermutationDistance[i][j] = -1
            }
        }
        cornersPermutationDistance[0][0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in cornersPermutationDistance.indices) {
                for (j in 0 until cornersPermutationDistance[i].length) {
                    if (cornersPermutationDistance[i][j] == depth) {
                        for (k in moves2.indices) {
                            val nextPermutation = cornersPermutationMove[i][k]
                            val nextCombination = edgesCombinationMove[j][k]
                            if (cornersPermutationDistance[nextPermutation][nextCombination] < 0) {
                                cornersPermutationDistance[nextPermutation][nextCombination] = (depth + 1).toByte()
                                nVisited++
                            }
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        initialized = true
    }

    fun solve(state: State): Array<String?> {
        if (!initialized) {
            initialize()
        }

        // orientation
        val orientation = IndexMapping.zeroSumOrientationToIndex(state.orientation, 3)
        var depth = 0
        while (true) {
            val solution = ArrayList<Int>()
            if (search(orientation, depth, solution, -1)) {
                val sequence = ArrayList<String>()
                var state2 = state
                for (moveIndex in solution) {
                    sequence.add(moveNames1[moveIndex])
                    state2 = state2.multiply(moves1[moveIndex])
                }
                val solution2 = solve2(state2)
                Collections.addAll(sequence, *solution2)
                val sequenceArray = arrayOfNulls<String>(sequence.size)
                sequence.toArray(sequenceArray)
                return sequenceArray
            }
            depth++
        }
    }

    private fun search(orientation: Int, depth: Int, solution: ArrayList<Int>, lastFace: Int): Boolean {
        if (depth == 0) {
            return orientation == 0
        }
        if (orientationDistance[orientation] <= depth) {
            for (i in moves1.indices) {
                if (faces1[i] == lastFace) {
                    continue
                }
                solution.add(i)
                if (search(
                                orientationMove[orientation][i],
                                depth - 1,
                                solution,
                                faces1[i])) {
                    return true
                }
                solution.removeAt(solution.size - 1)
            }
        }
        return false
    }

    private fun solve2(state: State): Array<String?> {
        // edges permutation
        val edgesPermutation = IndexMapping.permutationToIndex(state.edgesPermutation)

        // edges combination
        val isTopEdge = BooleanArray(8)
        for (k in isTopEdge.indices) {
            isTopEdge[k] = state.edgesPermutation!![k] < 4
        }
        val edgesCombination = IndexMapping.combinationToIndex(isTopEdge, 4)

        // corners permutation
        val cornersPermutation = IndexMapping.permutationToIndex(state.cornersPermutation)

        // corners combination
        val isTopCorner = BooleanArray(8)
        for (k in isTopCorner.indices) {
            isTopCorner[k] = state.cornersPermutation!![k] < 4
        }
        val cornersCombination = IndexMapping.combinationToIndex(isTopCorner, 4)
        var depth = 0
        while (true) {
            val solution = ArrayList<Int>()
            if (search2(
                            edgesPermutation,
                            edgesCombination,
                            cornersPermutation,
                            cornersCombination,
                            depth,
                            solution,
                            -1)) {
                val sequence = arrayOfNulls<String>(solution.size)
                for (i in solution.indices) {
                    sequence[i] = moveNames2[solution[i]]
                }
                return sequence
            }
            depth++
        }
    }

    private fun search2(edgesPermutation: Int, edgesCombination: Int, cornersPermutation: Int, cornersCombination: Int, depth: Int, solution: ArrayList<Int>, lastFace: Int): Boolean {
        if (depth == 0) {
            return edgesPermutation == 0 && cornersPermutation == 0
        }
        if (edgesPermutationDistance[edgesPermutation][cornersCombination] <= depth &&
                cornersPermutationDistance[cornersPermutation][edgesCombination] <= depth) {
            for (i in moves2.indices) {
                if (faces2[i] == lastFace) {
                    continue
                }
                solution.add(i)
                if (search2(
                                edgesPermutationMove[edgesPermutation][i],
                                edgesCombinationMove[edgesCombination][i],
                                cornersPermutationMove[cornersPermutation][i],
                                cornersCombinationMove[cornersCombination][i],
                                depth - 1,
                                solution,
                                faces2[i])) {
                    return true
                }
                solution.removeAt(solution.size - 1)
            }
        }
        return false
    }

    fun generate(state: State): Array<String?> {
        val solution = solve(state)
        val inverseMoveNames = HashMap<String?, String>()
        inverseMoveNames["Uw"] = "Uw'"
        inverseMoveNames["Uw2"] = "Uw2"
        inverseMoveNames["Uw'"] = "Uw"
        inverseMoveNames["Dw"] = "Dw'"
        inverseMoveNames["Dw2"] = "Dw2"
        inverseMoveNames["Dw'"] = "Dw"
        inverseMoveNames["Lw"] = "Lw'"
        inverseMoveNames["Lw2"] = "Lw2"
        inverseMoveNames["Lw'"] = "Lw"
        inverseMoveNames["Rw"] = "Rw'"
        inverseMoveNames["Rw2"] = "Rw2"
        inverseMoveNames["Rw'"] = "Rw"
        inverseMoveNames["Fw"] = "Fw'"
        inverseMoveNames["Fw2"] = "Fw2"
        inverseMoveNames["Fw'"] = "Fw"
        inverseMoveNames["Bw"] = "Bw'"
        inverseMoveNames["Bw2"] = "Bw2"
        inverseMoveNames["Bw'"] = "Bw"
        inverseMoveNames["U"] = "U'"
        inverseMoveNames["U2"] = "U2"
        inverseMoveNames["U'"] = "U"
        inverseMoveNames["D"] = "D'"
        inverseMoveNames["D2"] = "D2"
        inverseMoveNames["D'"] = "D"
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in sequence.indices) {
            sequence[i] = inverseMoveNames[solution[solution.size - 1 - i]]
        }
        return sequence
    }

    fun getRandomState(random: Random): State {
        val orientation = IndexMapping.indexToZeroSumOrientation(
                random.nextInt(N_ORIENTATIONS), 3, 8)
        val edgesPermutation = IndexMapping.indexToPermutation(
                random.nextInt(N_EDGES_PERMUTATIONS), 8)
        val cornersPermutation = IndexMapping.indexToPermutation(
                random.nextInt(N_CORNERS_PERMUTATIONS), 8)
        return State(orientation, edgesPermutation, cornersPermutation)
    }
}