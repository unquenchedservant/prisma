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

object RubiksCubeRUSolver {
    // moves
    private var moveNames: Array<String>
    private var moves: Array<RubiksCubeSolver.State?>

    // constants
    private const val N_CORNERS_PERMUTATIONS = 720
    private const val N_CORNERS_ORIENTATIONS = 729
    private const val N_EDGES_PERMUTATIONS = 5040
    private const val N_EDGES_ORIENTATIONS = 128
    private var goalCornersPermutation = 0
    private var goalCornersOrientation = 0
    private var goalEdgesPermutation = 0
    private var goalEdgesOrientation = 0

    // move tables
    private var cornersPermutationMove: Array<IntArray>
    private var cornersOrientationMove: Array<IntArray>
    private var edgesPermutationMove: Array<IntArray>
    private var edgesOrientationMove: Array<IntArray>
    private fun stateToIndices(state: RubiksCubeSolver.State?): IntArray {
        // corners
        val selectedCorners = booleanArrayOf(
                true, true, true, true,
                false, true, true, false)
        val cornersMapping = byteArrayOf(
                0, 1, 2, 3,
                -1, 5, 6, -1)
        val cornersPermutation = ByteArray(6)
        val cornersOrientation = ByteArray(6)
        var next = 0
        for (i in state!!.cornersPermutation!!.indices) {
            if (selectedCorners[i]) {
                cornersPermutation[next] = cornersMapping[state.cornersPermutation!![i].toInt()]
                cornersOrientation[next] = state.cornersOrientation!![i]
                next++
            }
        }
        val cornersPermutationIndex = IndexMapping.permutationToIndex(cornersPermutation)
        val cornersOrientationIndex = IndexMapping.orientationToIndex(cornersOrientation, 3)

        // edges
        val selectedEdges = booleanArrayOf(
                false, true, true, false,
                true, true, true, true,
                false, true, false, false)
        val edgesMapping = byteArrayOf(
                -1, 0, 1, -1,
                2, 3, 4, 5,
                -1, 6, -1, -1)
        val edgesPermutation = ByteArray(7)
        val edgesOrientation = ByteArray(7)
        next = 0
        for (i in state.edgesPermutation!!.indices) {
            if (selectedEdges[i]) {
                edgesPermutation[next] = edgesMapping[state.edgesPermutation!![i].toInt()]
                edgesOrientation[next] = state.edgesOrientation!![i]
                next++
            }
        }
        val edgesPermutationIndex = IndexMapping.permutationToIndex(edgesPermutation)
        val edgesOrientationIndex = IndexMapping.orientationToIndex(edgesOrientation, 2)
        return intArrayOf(
                cornersPermutationIndex,
                cornersOrientationIndex,
                edgesPermutationIndex,
                edgesOrientationIndex)
    }

    private fun indicesToState(indices: IntArray): RubiksCubeSolver.State {
        // corners
        var combination = booleanArrayOf(
                true, true, true, true,
                false, true, true, false)
        var permutation = IndexMapping.indexToPermutation(indices[0], 6)
        var orientation = IndexMapping.indexToOrientation(indices[1], 3, 6)
        val selectedCorners = byteArrayOf(0, 1, 2, 3, 5, 6)
        var nextSelectedCornerIndex = 0
        val otherCorners = byteArrayOf(4, 7)
        var nextOtherCornerIndex = 0
        val cornersPermutation = ByteArray(8)
        val cornersOrientation = ByteArray(8)
        for (i in cornersPermutation.indices) {
            if (combination[i]) {
                cornersPermutation[i] = selectedCorners[permutation!![nextSelectedCornerIndex].toInt()]
                cornersOrientation[i] = orientation!![nextSelectedCornerIndex]
                nextSelectedCornerIndex++
            } else {
                cornersPermutation[i] = otherCorners[nextOtherCornerIndex]
                cornersOrientation[i] = 0
                nextOtherCornerIndex++
            }
        }

        // edges
        combination = booleanArrayOf(
                false, true, true, false,
                true, true, true, true,
                false, true, false, false)
        permutation = IndexMapping.indexToPermutation(indices[2], 7)
        orientation = IndexMapping.indexToOrientation(indices[3], 2, 7)
        val selectedEdges = byteArrayOf(1, 2, 4, 5, 6, 7, 9)
        var nextSelectedEdgeIndex = 0
        val otherEdges = byteArrayOf(0, 3, 8, 10, 11)
        var nextOtherEdgeIndex = 0
        val edgesPermutation = ByteArray(12)
        val edgesOrientation = ByteArray(12)
        for (i in edgesPermutation.indices) {
            if (combination[i]) {
                edgesPermutation[i] = selectedEdges[permutation[nextSelectedEdgeIndex].toInt()]
                edgesOrientation[i] = orientation[nextSelectedEdgeIndex]
                nextSelectedEdgeIndex++
            } else {
                edgesPermutation[i] = otherEdges[nextOtherEdgeIndex]
                edgesOrientation[i] = 0
                nextOtherEdgeIndex++
            }
        }
        return RubiksCubeSolver.State(
                cornersPermutation,
                cornersOrientation,
                edgesPermutation,
                edgesOrientation)
    }

    // distance tables
    private var cornersDistance: Array<ByteArray>
    private var edgesDistance: Array<ByteArray>
    fun solve(state: RubiksCubeSolver.State?): Array<String?> {
        val indices = stateToIndices(state)
        var depth = 0
        while (true) {
            val solution = IntArray(depth)
            if (search(
                            indices[0],
                            indices[1],
                            indices[2],
                            indices[3],
                            depth,
                            solution)) {
                val sequence = arrayOfNulls<String>(solution.size)
                for (i in sequence.indices) {
                    sequence[i] = moveNames[solution[i]]
                }
                return sequence
            }
            depth++
        }
    }

    private fun search(
            cornersPermutation: Int,
            cornersOrientation: Int,
            edgesPermutation: Int,
            edgesOrientation: Int,
            depth: Int,
            solution: IntArray): Boolean {
        if (depth == 0) {
            return cornersPermutation == goalCornersPermutation && cornersOrientation == goalCornersOrientation && edgesPermutation == goalEdgesPermutation && edgesOrientation == goalEdgesOrientation
        }
        if (cornersDistance[cornersPermutation][cornersOrientation] > depth ||
                edgesDistance[edgesPermutation][edgesOrientation] > depth) {
            return false
        }
        for (i in moves.indices) {
            if (solution.size - depth > 0) {
                if (solution[solution.size - depth - 1] / 3 == i / 3) {
                    continue
                }
            }
            solution[solution.size - depth] = i
            if (search(
                            cornersPermutationMove[cornersPermutation][i],
                            cornersOrientationMove[cornersOrientation][i],
                            edgesPermutationMove[edgesPermutation][i],
                            edgesOrientationMove[edgesOrientation][i],
                            depth - 1,
                            solution)) {
                return true
            }
        }
        return false
    }

    fun generate(state: RubiksCubeSolver.State?): Array<String?> {
        val inverseMoveNames = HashMap<String?, String>()
        inverseMoveNames["U"] = "U'"
        inverseMoveNames["U2"] = "U2"
        inverseMoveNames["U'"] = "U"
        inverseMoveNames["R"] = "R'"
        inverseMoveNames["R2"] = "R2"
        inverseMoveNames["R'"] = "R"
        val solution = solve(state)
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in solution.indices) {
            sequence[i] = inverseMoveNames[solution[solution.size - i - 1]]
        }
        return sequence
    }

    fun getRandomState(random: Random): RubiksCubeSolver.State {
        while (true) {
            val cornersPermutation = random.nextInt(N_CORNERS_PERMUTATIONS)
            val cornersOrientation = random.nextInt(N_CORNERS_ORIENTATIONS)
            val edgesPermutation = random.nextInt(N_EDGES_PERMUTATIONS)
            val edgesOrientation = random.nextInt(N_EDGES_ORIENTATIONS)
            if (cornersDistance[cornersPermutation][cornersOrientation] < 0 ||
                    edgesDistance[edgesPermutation][edgesOrientation] < 0) {
                continue
            }
            val state = indicesToState(intArrayOf(
                    cornersPermutation,
                    cornersOrientation,
                    edgesPermutation,
                    edgesOrientation))
            if (permutationSign(state.cornersPermutation) ==
                    permutationSign(state.edgesPermutation)) {
                return state
            }
        }
    }

    private fun permutationSign(permutation: ByteArray?): Int {
        var nInversions = 0
        for (i in permutation!!.indices) {
            for (j in i + 1 until permutation.size) {
                if (permutation[i] > permutation[j]) {
                    nInversions++
                }
            }
        }
        return if (nInversions % 2 == 0) 1 else -1
    }

    init {
        moveNames = arrayOf(
                "U", "U2", "U'",
                "R", "R2", "R'")
        moves = arrayOfNulls(moveNames.size)
        for (i in moves.indices) {
            moves[i] = RubiksCubeSolver.State.Companion.moves!!.get(moveNames[i])
        }
    }

    init {
        val goalIndices = stateToIndices(RubiksCubeSolver.State.Companion.id)
        goalCornersPermutation = com.puzzletimer.solvers.goalIndices.get(0)
        goalCornersOrientation = com.puzzletimer.solvers.goalIndices.get(1)
        goalEdgesPermutation = com.puzzletimer.solvers.goalIndices.get(2)
        goalEdgesOrientation = com.puzzletimer.solvers.goalIndices.get(3)
    }

    init {
        // corners permutation
        cornersPermutationMove = Array(N_CORNERS_PERMUTATIONS) { IntArray(moves.size) }
        for (i in 0 until N_CORNERS_PERMUTATIONS) {
            val state = indicesToState(intArrayOf(i, 0, 0, 0))
            for (j in moves.indices) {
                val indices = stateToIndices(com.puzzletimer.solvers.state.multiply(moves[j]))
                cornersPermutationMove[i][j] = com.puzzletimer.solvers.indices.get(0)
            }
        }

        // corners orientation
        cornersOrientationMove = Array(N_CORNERS_ORIENTATIONS) { IntArray(moves.size) }
        for (i in 0 until N_CORNERS_ORIENTATIONS) {
            val state = indicesToState(intArrayOf(0, i, 0, 0))
            for (j in moves.indices) {
                val indices = stateToIndices(com.puzzletimer.solvers.state.multiply(moves[j]))
                cornersOrientationMove[i][j] = com.puzzletimer.solvers.indices.get(1)
            }
        }

        // edges permutation
        edgesPermutationMove = Array(N_EDGES_PERMUTATIONS) { IntArray(moves.size) }
        for (i in 0 until N_EDGES_PERMUTATIONS) {
            val state = indicesToState(intArrayOf(0, 0, i, 0))
            for (j in moves.indices) {
                val indices = stateToIndices(com.puzzletimer.solvers.state.multiply(moves[j]))
                edgesPermutationMove[i][j] = com.puzzletimer.solvers.indices.get(2)
            }
        }

        // edges orientation
        edgesOrientationMove = Array(N_EDGES_ORIENTATIONS) { IntArray(moves.size) }
        for (i in 0 until N_EDGES_ORIENTATIONS) {
            val state = indicesToState(intArrayOf(0, 0, 0, i))
            for (j in moves.indices) {
                val indices = stateToIndices(com.puzzletimer.solvers.state.multiply(moves[j]))
                edgesOrientationMove[i][j] = com.puzzletimer.solvers.indices.get(3)
            }
        }
    }

    init {
        // corners
        cornersDistance = Array(N_CORNERS_PERMUTATIONS) { ByteArray(N_CORNERS_ORIENTATIONS) }
        for (i in cornersDistance.indices) {
            for (j in 0 until cornersDistance[i].length) {
                cornersDistance[i][j] = -1
            }
        }
        cornersDistance[goalCornersPermutation][goalCornersOrientation] = 0
        val distance = 0
        var nVisited: Int
        do {
            com.puzzletimer.solvers.nVisited = 0
            for (i in cornersDistance.indices) {
                for (j in 0 until cornersDistance[i].length) {
                    if (cornersDistance[i][j] != com.puzzletimer.solvers.distance) {
                        continue
                    }
                    for (k in 0 until cornersPermutationMove[i].length) {
                        val nextPermutation = cornersPermutationMove[i][k]
                        val nextOrientation = cornersOrientationMove[j][k]
                        if (cornersDistance[com.puzzletimer.solvers.nextPermutation][com.puzzletimer.solvers.nextOrientation] < 0) {
                            cornersDistance[com.puzzletimer.solvers.nextPermutation][com.puzzletimer.solvers.nextOrientation] = (com.puzzletimer.solvers.distance + 1) as Byte
                            com.puzzletimer.solvers.nVisited++
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        } while (com.puzzletimer.solvers.nVisited > 0)

        // edges
        edgesDistance = Array(N_EDGES_PERMUTATIONS) { ByteArray(N_EDGES_ORIENTATIONS) }
        for (i in edgesDistance.indices) {
            for (j in 0 until edgesDistance[i].length) {
                edgesDistance[i][j] = -1
            }
        }
        edgesDistance[goalEdgesPermutation][goalEdgesOrientation] = 0
        com.puzzletimer.solvers.distance = 0
        do {
            com.puzzletimer.solvers.nVisited = 0
            for (i in edgesDistance.indices) {
                for (j in 0 until edgesDistance[i].length) {
                    if (edgesDistance[i][j] != com.puzzletimer.solvers.distance) {
                        continue
                    }
                    for (k in 0 until edgesPermutationMove[i].length) {
                        val nextPermutation = edgesPermutationMove[i][k]
                        val nextOrientation = edgesOrientationMove[j][k]
                        if (edgesDistance[com.puzzletimer.solvers.nextPermutation][com.puzzletimer.solvers.nextOrientation] < 0) {
                            edgesDistance[com.puzzletimer.solvers.nextPermutation][com.puzzletimer.solvers.nextOrientation] = (com.puzzletimer.solvers.distance + 1) as Byte
                            com.puzzletimer.solvers.nVisited++
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        } while (com.puzzletimer.solvers.nVisited > 0)
    }
}