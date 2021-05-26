// kociemba's two phase algorithm
// references: http://kociemba.org/cube.htm
//             http://www.jaapsch.net/puzzles/compcube.htm
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

object RubiksCubeSolver {
    // constants
    const val N_CORNERS_ORIENTATIONS = 2187
    const val N_EDGES_ORIENTATIONS = 2048
    const val N_E_EDGES_COMBINATIONS = 495
    const val N_CORNERS_PERMUTATIONS = 40320
    const val N_U_D_EDGES_PERMUTATIONS = 40320
    const val N_E_EDGES_PERMUTATIONS = 24
    const val N_EDGES_PERMUTATIONS = 479001600

    // moves
    private var moveNames1: Array<String>
    private var moves1: Array<State?>
    private var sides1: IntArray
    private var axes1: IntArray
    private var moveNames2: Array<String>
    private var moves2: Array<State?>
    private var sides2: IntArray
    private var axes2: IntArray

    // move tables
    private var cornersOrientationMove: Array<IntArray>
    private var edgesOrientationMove: Array<IntArray>
    private var eEdgesCombinationMove: Array<IntArray>
    private var cornersPermutationMove: Array<IntArray>
    private var uDEdgesPermutationMove: Array<IntArray>
    private var eEdgesPermutationMove: Array<IntArray>

    // prune tables
    private var cornersOrientationDistance: Array<ByteArray>
    private var edgesOrientationDistance: Array<ByteArray>
    private var cornersPermutationDistance: Array<ByteArray>
    private var uDEdgesPermutationDistance: Array<ByteArray>

    // search
    private const val MAX_SOLUTION_LENGTH = 23
    private const val MAX_PHASE_2_SOLUTION_LENGTH = 12
    private var initialState: State? = null
    private var solution1: ArrayList<Int>? = null
    private var solution2: ArrayList<Int>? = null
    private fun solution(state: State): Array<String?> {
        initialState = state

        // corners orientation index
        val cornersOrientation = IndexMapping.zeroSumOrientationToIndex(state.cornersOrientation, 3)

        // edges orientation index
        val edgesOrientation = IndexMapping.zeroSumOrientationToIndex(state.edgesOrientation, 2)

        // e edges combination index
        val isEEdge = BooleanArray(12)
        for (i in isEEdge.indices) {
            isEEdge[i] = state.edgesPermutation!![i] < 4
        }
        val eEdgesCombination = IndexMapping.combinationToIndex(isEEdge, 4)
        var depth = 0
        while (true) {
            solution1 = ArrayList(MAX_SOLUTION_LENGTH)
            if (search1(cornersOrientation, edgesOrientation, eEdgesCombination, depth)) {
                val sequence = ArrayList<String>()
                for (moveIndex in solution1!!) {
                    sequence.add(moveNames1[moveIndex])
                }
                for (moveIndex in solution2!!) {
                    sequence.add(moveNames2[moveIndex])
                }
                val sequenceArray = arrayOfNulls<String>(sequence.size)
                sequence.toArray(sequenceArray)
                return sequenceArray
            }
            depth++
        }
    }

    private fun search1(cornersOrientation: Int, edgesOrientation: Int, eEdgesCombinations: Int, depth: Int): Boolean {
        if (depth == 0) {
            if (cornersOrientation == 0 && edgesOrientation == 0 && eEdgesCombinations == 0) {
                var state = initialState
                for (moveIndex in solution1!!) {
                    state = state!!.multiply(moves1[moveIndex])
                }
                return solution2(state, MAX_SOLUTION_LENGTH - solution1!!.size)
            }
            return false
        }
        if (cornersOrientationDistance[cornersOrientation][eEdgesCombinations] <= depth &&
                edgesOrientationDistance[edgesOrientation][eEdgesCombinations] <= depth) {
            val lastMoves = intArrayOf(-1, -1)
            run {
                var i = 0
                while (i < lastMoves.size && i < solution1!!.size) {
                    lastMoves[i] = solution1!![solution1!!.size - 1 - i]
                    i++
                }
            }
            for (i in moves1.indices) {
                // same side
                if (lastMoves[0] >= 0 && sides1[i] == sides1[lastMoves[0]]) {
                    continue
                }

                // same axis three times in a row
                if (lastMoves[0] >= 0 && axes1[i] == axes1[lastMoves[0]] && lastMoves[1] >= 0 && axes1[i] == axes1[lastMoves[1]]) {
                    continue
                }
                solution1!!.add(i)
                if (search1(cornersOrientationMove[cornersOrientation][i],
                                edgesOrientationMove[edgesOrientation][i],
                                eEdgesCombinationMove[eEdgesCombinations][i],
                                depth - 1)) {
                    return true
                }
                solution1!!.removeAt(solution1!!.size - 1)
            }
        }
        return false
    }

    private fun solution2(state: State?, maxDepth: Int): Boolean {
        if (solution1!!.size > 0) {
            val lastMove = solution1!![solution1!!.size - 1]
            for (i in moveNames2.indices) {
                if (moveNames1[lastMove] == moveNames2[i]) {
                    return false
                }
            }
        }

        // corners permutation index
        val cornersPermutation = IndexMapping.permutationToIndex(state!!.cornersPermutation)

        // u and d eges permutation index
        val uDEdges = ByteArray(8)
        for (i in uDEdges.indices) {
            uDEdges[i] = (state.edgesPermutation!![i + 4] - 4).toByte()
        }
        val uDEdgesPermutation = IndexMapping.permutationToIndex(uDEdges)

        // e edges permutation index
        val eEdges = ByteArray(4)
        System.arraycopy(state.edgesPermutation, 0, eEdges, 0, eEdges.size)
        val eEdgesPermutation = IndexMapping.permutationToIndex(eEdges)
        for (depth in 0 until Math.min(MAX_PHASE_2_SOLUTION_LENGTH, maxDepth)) {
            solution2 = ArrayList(MAX_SOLUTION_LENGTH)
            if (search2(cornersPermutation, uDEdgesPermutation, eEdgesPermutation, depth)) {
                return true
            }
        }
        return false
    }

    private fun search2(cornersPermutation: Int, uDEdgesPermutation: Int, eEdgesPermutation: Int, depth: Int): Boolean {
        if (depth == 0) {
            return cornersPermutation == 0 && uDEdgesPermutation == 0 && eEdgesPermutation == 0
        }
        if (cornersPermutationDistance[cornersPermutation][eEdgesPermutation] <= depth &&
                uDEdgesPermutationDistance[uDEdgesPermutation][eEdgesPermutation] <= depth) {
            var lastSide = Int.MAX_VALUE
            if (solution2!!.size > 0) {
                lastSide = sides2[solution2!![solution2!!.size - 1]]
            }
            for (i in moves2.indices) {
                // avoid superflous moves between phases
                if (solution2!!.size == 0) {
                    var lastPhase1Axis = Int.MAX_VALUE
                    if (solution1!!.size > 0) {
                        lastPhase1Axis = axes1[solution1!![solution1!!.size - 1]]
                    }
                    if (axes2[i] == lastPhase1Axis) {
                        continue
                    }
                }

                // same side
                if (sides2[i] == lastSide) {
                    continue
                }
                solution2!!.add(i)
                if (search2(cornersPermutationMove[cornersPermutation][i],
                                uDEdgesPermutationMove[uDEdgesPermutation][i],
                                eEdgesPermutationMove[eEdgesPermutation][i],
                                depth - 1)) {
                    return true
                }
                solution2!!.removeAt(solution2!!.size - 1)
            }
        }
        return false
    }

    fun generate(state: State): Array<String?> {
        val solution = solution(state)
        val inverseMoveNames = HashMap<String?, String>()
        inverseMoveNames["U"] = "U'"
        inverseMoveNames["U2"] = "U2"
        inverseMoveNames["U'"] = "U"
        inverseMoveNames["D"] = "D'"
        inverseMoveNames["D2"] = "D2"
        inverseMoveNames["D'"] = "D"
        inverseMoveNames["L"] = "L'"
        inverseMoveNames["L2"] = "L2"
        inverseMoveNames["L'"] = "L"
        inverseMoveNames["R"] = "R'"
        inverseMoveNames["R2"] = "R2"
        inverseMoveNames["R'"] = "R"
        inverseMoveNames["F"] = "F'"
        inverseMoveNames["F2"] = "F2"
        inverseMoveNames["F'"] = "F"
        inverseMoveNames["B"] = "B'"
        inverseMoveNames["B2"] = "B2"
        inverseMoveNames["B'"] = "B"
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in solution.indices) {
            sequence[i] = inverseMoveNames[solution[solution.size - i - 1]]
        }
        return sequence
    }

    class State(var cornersPermutation: ByteArray?, var cornersOrientation: ByteArray?, var edgesPermutation: ByteArray?, var edgesOrientation: ByteArray?) {
        fun multiply(move: State?): State {
            // corners
            val cornersPermutation = ByteArray(8)
            val cornersOrientation = ByteArray(8)
            for (i in 0..7) {
                cornersPermutation[i] = this.cornersPermutation!![move!!.cornersPermutation!![i].toInt()]
                cornersOrientation[i] = ((this.cornersOrientation!![move.cornersPermutation!![i].toInt()] + move.cornersOrientation!![i]) % 3).toByte()
            }

            // edges
            val edgesPermutation = ByteArray(12)
            val edgesOrientation = ByteArray(12)
            for (i in 0..11) {
                edgesPermutation[i] = this.edgesPermutation!![move!!.edgesPermutation!![i].toInt()]
                edgesOrientation[i] = ((this.edgesOrientation!![move.edgesPermutation!![i].toInt()] + move.edgesOrientation!![i]) % 2).toByte()
            }
            return State(cornersPermutation, cornersOrientation, edgesPermutation, edgesOrientation)
        }

        fun applySequence(sequence: Array<String>): State {
            var state = this
            for (move in sequence) {
                state = state.multiply(moves!![move])
            }
            return state
        }

        companion object {
            var moves: HashMap<String, State>? = null
            var id: State? = null

            init {
                val moveU = State(byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 7, 4, 5, 6, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                val moveD = State(byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 8), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                val moveL = State(byteArrayOf(4, 1, 2, 0, 7, 5, 6, 3), byteArrayOf(2, 0, 0, 1, 1, 0, 0, 2), byteArrayOf(11, 1, 2, 7, 4, 5, 6, 0, 8, 9, 10, 3), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                val moveR = State(byteArrayOf(0, 2, 6, 3, 4, 1, 5, 7), byteArrayOf(0, 1, 2, 0, 0, 2, 1, 0), byteArrayOf(0, 5, 9, 3, 4, 2, 6, 7, 8, 1, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                val moveF = State(byteArrayOf(0, 1, 3, 7, 4, 5, 2, 6), byteArrayOf(0, 0, 1, 2, 0, 0, 2, 1), byteArrayOf(0, 1, 6, 10, 4, 5, 3, 7, 8, 9, 2, 11), byteArrayOf(0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0))
                val moveB = State(byteArrayOf(1, 5, 2, 3, 0, 4, 6, 7), byteArrayOf(1, 2, 0, 0, 2, 1, 0, 0), byteArrayOf(4, 8, 2, 3, 1, 5, 6, 7, 0, 9, 10, 11), byteArrayOf(1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0))
                moves = HashMap()
                moves!!["U"] = com.puzzletimer.solvers.moveU
                moves!!["U2"] = com.puzzletimer.solvers.moveU.multiply(com.puzzletimer.solvers.moveU)
                moves!!["U'"] = com.puzzletimer.solvers.moveU.multiply(com.puzzletimer.solvers.moveU).multiply(com.puzzletimer.solvers.moveU)
                moves!!["D"] = com.puzzletimer.solvers.moveD
                moves!!["D2"] = com.puzzletimer.solvers.moveD.multiply(com.puzzletimer.solvers.moveD)
                moves!!["D'"] = com.puzzletimer.solvers.moveD.multiply(com.puzzletimer.solvers.moveD).multiply(com.puzzletimer.solvers.moveD)
                moves!!["L"] = com.puzzletimer.solvers.moveL
                moves!!["L2"] = com.puzzletimer.solvers.moveL.multiply(com.puzzletimer.solvers.moveL)
                moves!!["L'"] = com.puzzletimer.solvers.moveL.multiply(com.puzzletimer.solvers.moveL).multiply(com.puzzletimer.solvers.moveL)
                moves!!["R"] = com.puzzletimer.solvers.moveR
                moves!!["R2"] = com.puzzletimer.solvers.moveR.multiply(com.puzzletimer.solvers.moveR)
                moves!!["R'"] = com.puzzletimer.solvers.moveR.multiply(com.puzzletimer.solvers.moveR).multiply(com.puzzletimer.solvers.moveR)
                moves!!["F"] = com.puzzletimer.solvers.moveF
                moves!!["F2"] = com.puzzletimer.solvers.moveF.multiply(com.puzzletimer.solvers.moveF)
                moves!!["F'"] = com.puzzletimer.solvers.moveF.multiply(com.puzzletimer.solvers.moveF).multiply(com.puzzletimer.solvers.moveF)
                moves!!["B"] = com.puzzletimer.solvers.moveB
                moves!!["B2"] = com.puzzletimer.solvers.moveB.multiply(com.puzzletimer.solvers.moveB)
                moves!!["B'"] = com.puzzletimer.solvers.moveB.multiply(com.puzzletimer.solvers.moveB).multiply(com.puzzletimer.solvers.moveB)
            }

            init {
                id = State(
                        IndexMapping.indexToPermutation(0, 8),
                        IndexMapping.indexToOrientation(0, 3, 8),
                        IndexMapping.indexToPermutation(0, 12),
                        IndexMapping.indexToOrientation(0, 2, 12))
            }
        }
    }

    init {
        // phase 1
        moveNames1 = arrayOf(
                "U", "U2", "U'",
                "D", "D2", "D'",
                "L", "L2", "L'",
                "R", "R2", "R'",
                "F", "F2", "F'",
                "B", "B2", "B'")
        moves1 = arrayOfNulls(moveNames1.size)
        for (i in moves1.indices) {
            moves1[i] = State.moves!![moveNames1[i]]
        }
        sides1 = intArrayOf(
                0, 0, 0,
                1, 1, 1,
                2, 2, 2,
                3, 3, 3,
                4, 4, 4,
                5, 5, 5)
        axes1 = intArrayOf(
                0, 0, 0,
                0, 0, 0,
                1, 1, 1,
                1, 1, 1,
                2, 2, 2,
                2, 2, 2)

        // phase 2
        moveNames2 = arrayOf(
                "U", "U2", "U'",
                "D", "D2", "D'",
                "L2",
                "R2",
                "F2",
                "B2")
        moves2 = arrayOfNulls(moveNames2.size)
        for (i in moves2.indices) {
            moves2[i] = State.moves!![moveNames2[i]]
        }
        sides2 = intArrayOf(
                0, 0, 0,
                1, 1, 1,
                2,
                3,
                4,
                5)
        axes2 = intArrayOf(
                0, 0, 0,
                0, 0, 0,
                1,
                1,
                2,
                2)
    }

    init {
        // phase 1
        cornersOrientationMove = Array(N_CORNERS_ORIENTATIONS) { IntArray(moves1.size) }
        for (i in 0 until N_CORNERS_ORIENTATIONS) {
            val state = State(ByteArray(8), IndexMapping.indexToZeroSumOrientation(i, 3, 8), ByteArray(12), ByteArray(12))
            for (j in moves1.indices) {
                cornersOrientationMove[i][j] = IndexMapping.zeroSumOrientationToIndex(com.puzzletimer.solvers.state.multiply(moves1[j]).cornersOrientation, 3)
            }
        }
        edgesOrientationMove = Array(N_EDGES_ORIENTATIONS) { IntArray(moves1.size) }
        for (i in 0 until N_EDGES_ORIENTATIONS) {
            val state = State(ByteArray(8), ByteArray(8), ByteArray(12), IndexMapping.indexToZeroSumOrientation(i, 2, 12))
            for (j in moves1.indices) {
                edgesOrientationMove[i][j] = IndexMapping.zeroSumOrientationToIndex(com.puzzletimer.solvers.state.multiply(moves1[j]).edgesOrientation, 2)
            }
        }
        eEdgesCombinationMove = Array(N_E_EDGES_COMBINATIONS) { IntArray(moves1.size) }
        for (i in 0 until N_E_EDGES_COMBINATIONS) {
            val combination = IndexMapping.indexToCombination(i, 4, 12)
            val edges = ByteArray(12)
            val nextE: Byte = 0
            val nextUD: Byte = 4
            for (j in com.puzzletimer.solvers.edges.indices) {
                if (com.puzzletimer.solvers.combination.get(j)) {
                    com.puzzletimer.solvers.edges.get(j) = com.puzzletimer.solvers.nextE++
                } else {
                    com.puzzletimer.solvers.edges.get(j) = com.puzzletimer.solvers.nextUD++
                }
            }
            val state = State(ByteArray(8), ByteArray(8), com.puzzletimer.solvers.edges, ByteArray(12))
            for (j in moves1.indices) {
                val result: State = com.puzzletimer.solvers.state.multiply(moves1[j])
                val isEEdge = BooleanArray(12)
                for (k in com.puzzletimer.solvers.isEEdge.indices) {
                    com.puzzletimer.solvers.isEEdge.get(k) = com.puzzletimer.solvers.result.edgesPermutation.get(k) < 4
                }
                eEdgesCombinationMove[i][j] = IndexMapping.combinationToIndex(com.puzzletimer.solvers.isEEdge, 4)
            }
        }


        // phase 2
        cornersPermutationMove = Array(N_CORNERS_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in 0 until N_CORNERS_PERMUTATIONS) {
            val state = State(IndexMapping.indexToPermutation(i, 8), ByteArray(8), ByteArray(12), ByteArray(12))
            for (j in moves2.indices) {
                cornersPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.state.multiply(moves2[j]).cornersPermutation)
            }
        }
        uDEdgesPermutationMove = Array(N_U_D_EDGES_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in 0 until N_U_D_EDGES_PERMUTATIONS) {
            val permutation = IndexMapping.indexToPermutation(i, 8)
            val edges = ByteArray(12)
            for (j in com.puzzletimer.solvers.edges.indices) {
                com.puzzletimer.solvers.edges.get(j) = if (j >= 4) com.puzzletimer.solvers.permutation.get(j - 4) else j
            }
            val state = State(ByteArray(8), ByteArray(8), com.puzzletimer.solvers.edges, ByteArray(12))
            for (j in moves2.indices) {
                val result: State = com.puzzletimer.solvers.state.multiply(moves2[j])
                val uDEdges = ByteArray(8)
                for (k in com.puzzletimer.solvers.uDEdges.indices) {
                    com.puzzletimer.solvers.uDEdges.get(k) = (com.puzzletimer.solvers.result.edgesPermutation.get(k + 4) - 4)
                }
                uDEdgesPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.uDEdges)
            }
        }
        eEdgesPermutationMove = Array(N_E_EDGES_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in 0 until N_E_EDGES_PERMUTATIONS) {
            val permutation = IndexMapping.indexToPermutation(i, 4)
            val edges = ByteArray(12)
            for (j in com.puzzletimer.solvers.edges.indices) {
                com.puzzletimer.solvers.edges.get(j) = if (j >= 4) j else com.puzzletimer.solvers.permutation.get(j)
            }
            val state = State(ByteArray(8), ByteArray(8), com.puzzletimer.solvers.edges, ByteArray(12))
            for (j in moves2.indices) {
                val result: State = com.puzzletimer.solvers.state.multiply(moves2[j])
                val eEdges = ByteArray(4)
                System.arraycopy(com.puzzletimer.solvers.result.edgesPermutation, 0, com.puzzletimer.solvers.eEdges, 0, com.puzzletimer.solvers.eEdges.size)
                eEdgesPermutationMove[i][j] = IndexMapping.permutationToIndex(com.puzzletimer.solvers.eEdges)
            }
        }
    }

    init {
        // phase 1
        cornersOrientationDistance = Array(N_CORNERS_ORIENTATIONS) { ByteArray(N_E_EDGES_COMBINATIONS) }
        for (i in cornersOrientationDistance.indices) {
            for (j in 0 until cornersOrientationDistance[i].length) {
                cornersOrientationDistance[i][j] = -1
            }
        }
        cornersOrientationDistance[0][0] = 0
        val distance = 0
        val nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_CORNERS_ORIENTATIONS * N_E_EDGES_COMBINATIONS) {
            for (i in 0 until N_CORNERS_ORIENTATIONS) {
                for (j in 0 until N_E_EDGES_COMBINATIONS) {
                    if (cornersOrientationDistance[i][j] == com.puzzletimer.solvers.distance) {
                        for (k in moves1.indices) {
                            val nextCornersOrientation = cornersOrientationMove[i][k]
                            val nextEEdgesCombination = eEdgesCombinationMove[j][k]
                            if (cornersOrientationDistance[com.puzzletimer.solvers.nextCornersOrientation][com.puzzletimer.solvers.nextEEdgesCombination] < 0) {
                                cornersOrientationDistance[com.puzzletimer.solvers.nextCornersOrientation][com.puzzletimer.solvers.nextEEdgesCombination] = (com.puzzletimer.solvers.distance + 1) as Byte
                                com.puzzletimer.solvers.nVisited++
                            }
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }
        edgesOrientationDistance = Array(N_EDGES_ORIENTATIONS) { ByteArray(N_E_EDGES_COMBINATIONS) }
        for (i in edgesOrientationDistance.indices) {
            for (j in 0 until edgesOrientationDistance[i].length) {
                edgesOrientationDistance[i][j] = -1
            }
        }
        edgesOrientationDistance[0][0] = 0
        com.puzzletimer.solvers.distance = 0
        com.puzzletimer.solvers.nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_EDGES_ORIENTATIONS * N_E_EDGES_COMBINATIONS) {
            for (i in 0 until N_EDGES_ORIENTATIONS) {
                for (j in 0 until N_E_EDGES_COMBINATIONS) {
                    if (edgesOrientationDistance[i][j] == com.puzzletimer.solvers.distance) {
                        for (k in moves1.indices) {
                            val nextEdgesOrientation = edgesOrientationMove[i][k]
                            val nextEEdgesCombination = eEdgesCombinationMove[j][k]
                            if (edgesOrientationDistance[com.puzzletimer.solvers.nextEdgesOrientation][com.puzzletimer.solvers.nextEEdgesCombination] < 0) {
                                edgesOrientationDistance[com.puzzletimer.solvers.nextEdgesOrientation][com.puzzletimer.solvers.nextEEdgesCombination] = (com.puzzletimer.solvers.distance + 1) as Byte
                                com.puzzletimer.solvers.nVisited++
                            }
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }


        // phase 2
        cornersPermutationDistance = Array(N_CORNERS_PERMUTATIONS) { ByteArray(N_E_EDGES_PERMUTATIONS) }
        for (i in cornersPermutationDistance.indices) {
            for (j in 0 until cornersPermutationDistance[i].length) {
                cornersPermutationDistance[i][j] = -1
            }
        }
        cornersPermutationDistance[0][0] = 0
        com.puzzletimer.solvers.distance = 0
        com.puzzletimer.solvers.nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_CORNERS_PERMUTATIONS * N_E_EDGES_PERMUTATIONS) {
            for (i in 0 until N_CORNERS_PERMUTATIONS) {
                for (j in 0 until N_E_EDGES_PERMUTATIONS) {
                    if (cornersPermutationDistance[i][j] == com.puzzletimer.solvers.distance) {
                        for (k in moves2.indices) {
                            val nextCornersPermutation = cornersPermutationMove[i][k]
                            val nextEEdgesPermutation = eEdgesPermutationMove[j][k]
                            if (cornersPermutationDistance[com.puzzletimer.solvers.nextCornersPermutation][com.puzzletimer.solvers.nextEEdgesPermutation] < 0) {
                                cornersPermutationDistance[com.puzzletimer.solvers.nextCornersPermutation][com.puzzletimer.solvers.nextEEdgesPermutation] = (com.puzzletimer.solvers.distance + 1) as Byte
                                com.puzzletimer.solvers.nVisited++
                            }
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }
        uDEdgesPermutationDistance = Array(N_U_D_EDGES_PERMUTATIONS) { ByteArray(N_E_EDGES_PERMUTATIONS) }
        for (i in uDEdgesPermutationDistance.indices) {
            for (j in 0 until uDEdgesPermutationDistance[i].length) {
                uDEdgesPermutationDistance[i][j] = -1
            }
        }
        uDEdgesPermutationDistance[0][0] = 0
        com.puzzletimer.solvers.distance = 0
        com.puzzletimer.solvers.nVisited = 1
        while (com.puzzletimer.solvers.nVisited < N_U_D_EDGES_PERMUTATIONS * N_E_EDGES_PERMUTATIONS) {
            for (i in 0 until N_U_D_EDGES_PERMUTATIONS) {
                for (j in 0 until N_E_EDGES_PERMUTATIONS) {
                    if (uDEdgesPermutationDistance[i][j] == com.puzzletimer.solvers.distance) {
                        for (k in moves2.indices) {
                            val nextUDEdgesPermutation = uDEdgesPermutationMove[i][k]
                            val nextEEdgesPermutation = eEdgesPermutationMove[j][k]
                            if (uDEdgesPermutationDistance[com.puzzletimer.solvers.nextUDEdgesPermutation][com.puzzletimer.solvers.nextEEdgesPermutation] < 0) {
                                uDEdgesPermutationDistance[com.puzzletimer.solvers.nextUDEdgesPermutation][com.puzzletimer.solvers.nextEEdgesPermutation] = (com.puzzletimer.solvers.distance + 1) as Byte
                                com.puzzletimer.solvers.nVisited++
                            }
                        }
                    }
                }
            }
            com.puzzletimer.solvers.distance++
        }
    }
}