package com.puzzletimer.solvers

import com.puzzletimer.solvers.FloppyCubeSolver
import com.puzzletimer.solvers.IndexMapping
import java.util.HashMap
import com.puzzletimer.solvers.RubiksClockSolver
import com.puzzletimer.solvers.RubiksCubeSolver
import com.puzzletimer.solvers.RubiksCubeCrossSolver
import com.puzzletimer.solvers.RubiksCubeRUSolver
import com.puzzletimer.solvers.RubiksCubeXCrossSolver
import com.puzzletimer.solvers.RubiksDominoSolver
import java.util.Collections
import com.puzzletimer.solvers.Square1ShapeSolver
import com.puzzletimer.solvers.Square1Solver.CubeState
import com.puzzletimer.solvers.TowerCubeSolver
import java.util.ArrayList

object RubiksCubeCrossSolver {
    // moves
    private var moveNames: Array<String>
    private var moves: Array<RubiksCubeSolver.State?>

    // constants
    private const val N_EDGES_COMBINATIONS = 495
    private const val N_EDGES_PERMUTATIONS = 24
    private const val N_EDGES_ORIENTATIONS = 16
    private var goalEdgesPermutation = 0
    private var goalEdgesOrientation = 0

    // move tables
    private var edgesPermutationMove: Array<IntArray>
    private var edgesOrientationMove: Array<IntArray>
    private fun stateToIndices(state: RubiksCubeSolver.State?): IntArray {
        // edges
        val selectedEdges = booleanArrayOf(
                false, false, false, false,
                false, false, false, false,
                true, true, true, true)
        val edgesMapping = byteArrayOf(
                -1, -1, -1, -1,
                -1, -1, -1, -1,
                0, 1, 2, 3)
        val edgesCombination = BooleanArray(state!!.edgesPermutation!!.size)
        for (i in edgesCombination.indices) {
            edgesCombination[i] = selectedEdges[state.edgesPermutation!![i].toInt()]
        }
        val edgesCombinationIndex = IndexMapping.combinationToIndex(edgesCombination, 4)
        val edgesPermutation = ByteArray(4)
        val edgesOrientation = ByteArray(4)
        var next = 0
        for (i in state.edgesPermutation!!.indices) {
            if (edgesCombination[i]) {
                edgesPermutation[next] = edgesMapping[state.edgesPermutation!![i].toInt()]
                edgesOrientation[next] = state.edgesOrientation!![i]
                next++
            }
        }
        val edgesPermutationIndex = IndexMapping.permutationToIndex(edgesPermutation)
        val edgesOrientationIndex = IndexMapping.orientationToIndex(edgesOrientation, 2)
        return intArrayOf(
                edgesCombinationIndex,
                edgesPermutationIndex,
                edgesOrientationIndex)
    }

    private fun indicesToState(indices: IntArray): RubiksCubeSolver.State {
        val combination = IndexMapping.indexToCombination(indices[0], 4, 12)
        val permutation = IndexMapping.indexToPermutation(indices[1], 4)
        val orientation = IndexMapping.indexToOrientation(indices[2], 2, 4)
        val selectedEdges = byteArrayOf(8, 9, 10, 11)
        var nextSelectedEdgeIndex = 0
        val otherEdges = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        var nextOtherEdgeIndex = 0
        val edgesPermutation = ByteArray(12)
        val edgesOrientation = ByteArray(12)
        for (i in edgesPermutation.indices) {
            if (combination!![i]) {
                edgesPermutation[i] = selectedEdges[permutation!![nextSelectedEdgeIndex].toInt()]
                edgesOrientation[i] = orientation!![nextSelectedEdgeIndex]
                nextSelectedEdgeIndex++
            } else {
                edgesPermutation[i] = otherEdges[nextOtherEdgeIndex]
                edgesOrientation[i] = 0
                nextOtherEdgeIndex++
            }
        }
        return RubiksCubeSolver.State(
                RubiksCubeSolver.State.Companion.id!!.cornersPermutation,
                RubiksCubeSolver.State.Companion.id!!.cornersOrientation,
                edgesPermutation,
                edgesOrientation)
    }

    // distance tables
    private var edgesPermutationDistance: ByteArray
    private var edgesOrientationDistance: ByteArray
    fun solve(state: RubiksCubeSolver.State?): ArrayList<Array<String?>> {
        val indices = stateToIndices(state)
        val edgesPermutationIndex = indices[0] * N_EDGES_PERMUTATIONS + indices[1]
        val edgesOrientationIndex = indices[0] * N_EDGES_ORIENTATIONS + indices[2]
        val solutions = ArrayList<Array<String?>>()
        var depth = 0
        while (true) {
            val path = IntArray(depth)
            search(edgesPermutationIndex,
                    edgesOrientationIndex,
                    depth,
                    path,
                    solutions)
            if (solutions.size > 0) {
                return solutions
            }
            depth++
        }
    }

    private fun search(
            edgesPermutation: Int,
            edgesOrientation: Int,
            depth: Int,
            path: IntArray,
            solutions: ArrayList<Array<String?>>) {
        if (depth == 0) {
            if (edgesPermutation == goalEdgesPermutation &&
                    edgesOrientation == goalEdgesOrientation) {
                val sequence = arrayOfNulls<String>(path.size)
                for (i in sequence.indices) {
                    sequence[i] = moveNames[path[i]]
                }
                solutions.add(sequence)
            }
            return
        }
        if (edgesPermutationDistance[edgesPermutation] > depth ||
                edgesOrientationDistance[edgesOrientation] > depth) {
            return
        }
        for (i in moves.indices) {
            path[path.size - depth] = i
            search(
                    edgesPermutationMove[edgesPermutation][i],
                    edgesOrientationMove[edgesOrientation][i],
                    depth - 1,
                    path,
                    solutions)
        }
    }

    init {
        moveNames = arrayOf(
                "U", "U2", "U'",
                "D", "D2", "D'",
                "L", "L2", "L'",
                "R", "R2", "R'",
                "F", "F2", "F'",
                "B", "B2", "B'")
        moves = arrayOfNulls(moveNames.size)
        for (i in moves.indices) {
            moves[i] = RubiksCubeSolver.State.Companion.moves!!.get(moveNames[i])
        }
    }

    init {
        val goalIndices = stateToIndices(RubiksCubeSolver.State.Companion.id)
        goalEdgesPermutation = com.puzzletimer.solvers.goalIndices.get(0) * N_EDGES_PERMUTATIONS + com.puzzletimer.solvers.goalIndices.get(1)
        goalEdgesOrientation = com.puzzletimer.solvers.goalIndices.get(0) * N_EDGES_ORIENTATIONS + com.puzzletimer.solvers.goalIndices.get(2)
    }

    init {
        // edges permutation
        edgesPermutationMove = Array(N_EDGES_COMBINATIONS * N_EDGES_PERMUTATIONS) { IntArray(moves.size) }
        for (i in 0 until N_EDGES_COMBINATIONS) {
            for (j in 0 until N_EDGES_PERMUTATIONS) {
                val state = indicesToState(intArrayOf(i, j, 0))
                for (k in moves.indices) {
                    val indices = stateToIndices(com.puzzletimer.solvers.state.multiply(moves[k]))
                    edgesPermutationMove[i * N_EDGES_PERMUTATIONS + j][k] = com.puzzletimer.solvers.indices.get(0) * N_EDGES_PERMUTATIONS + com.puzzletimer.solvers.indices.get(1)
                }
            }
        }

        // edges orientation
        edgesOrientationMove = Array(N_EDGES_COMBINATIONS * N_EDGES_ORIENTATIONS) { IntArray(moves.size) }
        for (i in 0 until N_EDGES_COMBINATIONS) {
            for (j in 0 until N_EDGES_ORIENTATIONS) {
                val state = indicesToState(intArrayOf(i, 0, j))
                for (k in moves.indices) {
                    val indices = stateToIndices(com.puzzletimer.solvers.state.multiply(moves[k]))
                    edgesOrientationMove[i * N_EDGES_ORIENTATIONS + j][k] = com.puzzletimer.solvers.indices.get(0) * N_EDGES_ORIENTATIONS + com.puzzletimer.solvers.indices.get(2)
                }
            }
        }
    }

    init {
        // edges permutation
        edgesPermutationDistance = ByteArray(N_EDGES_COMBINATIONS * N_EDGES_PERMUTATIONS)
        for (i in edgesPermutationDistance.indices) {
            edgesPermutationDistance[i] = -1
        }
        edgesPermutationDistance[goalEdgesPermutation] = 0
        val distance = 0
        val nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_EDGES_COMBINATIONS * N_EDGES_PERMUTATIONS) {
            for (i in edgesPermutationDistance.indices) {
                if (edgesPermutationDistance[i] != com.puzzletimer.solvers.distance) {
                    continue
                }
                for (j in 0 until edgesPermutationMove[i].length) {
                    val next = edgesPermutationMove[i][j]
                    if (edgesPermutationDistance[com.puzzletimer.solvers.next] < 0) {
                        edgesPermutationDistance[com.puzzletimer.solvers.next] = (com.puzzletimer.solvers.distance + 1) as Byte
                        com.puzzletimer.solvers.nVisited++
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }


        // edges orientation
        edgesOrientationDistance = ByteArray(N_EDGES_COMBINATIONS * N_EDGES_ORIENTATIONS)
        for (i in edgesOrientationDistance.indices) {
            edgesOrientationDistance[i] = -1
        }
        edgesOrientationDistance[goalEdgesOrientation] = 0
        com.puzzletimer.solvers.distance = 0
        com.puzzletimer.solvers.nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_EDGES_COMBINATIONS * N_EDGES_ORIENTATIONS) {
            for (i in edgesOrientationDistance.indices) {
                if (edgesOrientationDistance[i] != com.puzzletimer.solvers.distance) {
                    continue
                }
                for (j in 0 until edgesOrientationMove[i].length) {
                    val next = edgesOrientationMove[i][j]
                    if (edgesOrientationDistance[com.puzzletimer.solvers.next] < 0) {
                        edgesOrientationDistance[com.puzzletimer.solvers.next] = (com.puzzletimer.solvers.distance + 1) as Byte
                        com.puzzletimer.solvers.nVisited++
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }
    }
}