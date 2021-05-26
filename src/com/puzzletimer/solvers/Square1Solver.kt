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

class Square1Solver {
    class State(var permutation: ByteArray) {
        val isTwistable: Boolean
            get() = permutation[1] != permutation[2] && permutation[7] != permutation[8] && permutation[13] != permutation[14] && permutation[19] != permutation[20]

        fun multiply(move: State?): State {
            val permutation = ByteArray(24)
            for (i in permutation.indices) {
                permutation[i] = this.permutation[move!!.permutation[i].toInt()]
            }
            return State(permutation)
        }

        val shapeIndex: Int
            get() {
                val cuts = ByteArray(24)
                for (i in cuts.indices) {
                    cuts[i] = 0
                }
                for (i in 0..11) {
                    val next = (i + 1) % 12
                    if (permutation[i] != permutation[next]) {
                        cuts[i] = 1
                    }
                }
                for (i in 0..11) {
                    val next = (i + 1) % 12
                    if (permutation[12 + i] != permutation[12 + next]) {
                        cuts[12 + i] = 1
                    }
                }
                return IndexMapping.orientationToIndex(cuts, 2)
            }
        val piecesPermutation: ByteArray
            get() {
                val permutation = ByteArray(16)
                var nextSlot = 0
                for (i in 0..11) {
                    val next = (i + 1) % 12
                    if (this.permutation[i] != this.permutation[next]) {
                        permutation[nextSlot++] = this.permutation[i]
                    }
                }
                for (i in 0..11) {
                    val next = 12 + (i + 1) % 12
                    if (this.permutation[12 + i] != this.permutation[next]) {
                        permutation[nextSlot++] = this.permutation[12 + i]
                    }
                }
                return permutation
            }

        fun toCubeState(): CubeState {
            val cornerIndices = intArrayOf(0, 3, 6, 9, 12, 15, 18, 21)
            val cornersPermutation = ByteArray(8)
            for (i in cornersPermutation.indices) {
                cornersPermutation[i] = permutation[cornerIndices[i]]
            }
            val edgeIndices = intArrayOf(1, 4, 7, 10, 13, 16, 19, 22)
            val edgesPermutation = ByteArray(8)
            for (i in edgesPermutation.indices) {
                edgesPermutation[i] = (permutation[edgeIndices[i]] - 8).toByte()
            }
            return CubeState(cornersPermutation, edgesPermutation)
        }

        companion object {
            var id: State? = null

            init {
                id = State(byteArrayOf(
                        0, 8, 1, 1, 9, 2, 2, 10, 3, 3, 11, 0,
                        4, 12, 5, 5, 13, 6, 6, 14, 7, 7, 15, 4))
            }
        }
    }

    class CubeState(var cornersPermutation: ByteArray?, var edgesPermutation: ByteArray?) {
        fun multiply(move: CubeState): CubeState {
            val cornersPermutation = ByteArray(8)
            val edgesPermutation = ByteArray(8)
            for (i in 0..7) {
                cornersPermutation[i] = this.cornersPermutation!![move.cornersPermutation!![i].toInt()]
                edgesPermutation[i] = this.edgesPermutation!![move.edgesPermutation!![i].toInt()]
            }
            return CubeState(cornersPermutation, edgesPermutation)
        }
    }

    private var initialized = false

    // phase 1
    private var moves1: Array<State?>
    private var shapes: ArrayList<State?>? = null
    private var evenShapeDistance: HashMap<Int, Int>? = null
    private var oddShapeDistance: HashMap<Int, Int>? = null

    // phase 2
    val N_CORNERS_PERMUTATIONS = 40320
    val N_CORNERS_COMBINATIONS = 70
    val N_EDGES_PERMUTATIONS = 40320
    val N_EDGES_COMBINATIONS = 70
    private var moves2: Array<CubeState>
    private var cornersPermutationMove: Array<IntArray>
    private var cornersCombinationMove: Array<IntArray>
    private var edgesPermutationMove: Array<IntArray>
    private var edgesCombinationMove: Array<IntArray>
    private var cornersDistance: Array<ByteArray>
    private var edgesDistance: Array<ByteArray>
    private fun initialize() {
        // -- phase 1 --

        // moves
        moves1 = arrayOfNulls(23)
        val move10 = State(byteArrayOf(
                11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23))
        var move = move10
        for (i in 0..10) {
            moves1[i] = move
            move = move.multiply(move10)
        }
        val move01 = State(byteArrayOf(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 12))
        move = move01
        for (i in 0..10) {
            moves1[11 + i] = move
            move = move.multiply(move01)
        }
        val moveTwist = State(byteArrayOf(
                0, 1, 19, 18, 17, 16, 15, 14, 8, 9, 10, 11,
                12, 13, 7, 6, 5, 4, 3, 2, 20, 21, 22, 23))
        moves1[22] = moveTwist

        // shape tables
        shapes = ArrayList()
        evenShapeDistance = HashMap()
        oddShapeDistance = HashMap()
        evenShapeDistance!![State.id!!.shapeIndex] = 0
        var fringe = ArrayList<State?>()
        fringe.add(State.id)
        var depth = 0
        while (fringe.size > 0) {
            val newFringe = ArrayList<State?>()
            for (state in fringe) {
                if (state!!.isTwistable) {
                    shapes!!.add(state)
                }
                for (i in moves1.indices) {
                    if (i == 22 && !state.isTwistable) {
                        continue
                    }
                    val next = state.multiply(moves1[i])
                    val distanceTable = if (isEvenPermutation(next.piecesPermutation)) evenShapeDistance else oddShapeDistance
                    if (!distanceTable!!.containsKey(next.shapeIndex)) {
                        distanceTable[next.shapeIndex] = depth + 1
                        newFringe.add(next)
                    }
                }
            }
            fringe = newFringe
            depth++
        }

        // -- phase 2 --

        // moves
        val move30 = CubeState(byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7))
        val move03 = CubeState(byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4))
        val moveTwistTop = CubeState(byteArrayOf(0, 6, 5, 3, 4, 2, 1, 7), byteArrayOf(6, 5, 2, 3, 4, 1, 0, 7))
        val moveTwistBottom = CubeState(byteArrayOf(0, 6, 5, 3, 4, 2, 1, 7), byteArrayOf(0, 5, 4, 3, 2, 1, 6, 7))
        moves2 = arrayOf(
                move30,
                move30.multiply(move30),
                move30.multiply(move30).multiply(move30),
                move03,
                move03.multiply(move03),
                move03.multiply(move03).multiply(move03),
                moveTwistTop,
                moveTwistBottom)

        // move tables
        cornersPermutationMove = Array(N_CORNERS_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in cornersPermutationMove.indices) {
            val state = CubeState(IndexMapping.indexToPermutation(i, 8), ByteArray(8))
            for (j in 0 until cornersPermutationMove[i].length) {
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
            val state = CubeState(corners, ByteArray(8))
            for (j in 0 until cornersCombinationMove[i].length) {
                val result = state.multiply(moves2[j])
                val isTopCorner = BooleanArray(8)
                for (k in isTopCorner.indices) {
                    isTopCorner[k] = result.cornersPermutation!![k] < 4
                }
                cornersCombinationMove[i][j] = IndexMapping.combinationToIndex(isTopCorner, 4)
            }
        }
        edgesPermutationMove = Array(N_EDGES_PERMUTATIONS) { IntArray(moves2.size) }
        for (i in edgesPermutationMove.indices) {
            val state = CubeState(ByteArray(8), IndexMapping.indexToPermutation(i, 8))
            for (j in 0 until edgesPermutationMove[i].length) {
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
            val state = CubeState(ByteArray(8), edges)
            for (j in 0 until edgesCombinationMove[i].length) {
                val result = state.multiply(moves2[j])
                val isTopEdge = BooleanArray(8)
                for (k in isTopEdge.indices) {
                    isTopEdge[k] = result.edgesPermutation!![k] < 4
                }
                edgesCombinationMove[i][j] = IndexMapping.combinationToIndex(isTopEdge, 4)
            }
        }

        // prune tables
        cornersDistance = Array(N_CORNERS_PERMUTATIONS) { ByteArray(N_EDGES_COMBINATIONS) }
        for (i in cornersDistance.indices) {
            for (j in 0 until cornersDistance[i].length) {
                cornersDistance[i][j] = -1
            }
        }
        cornersDistance[0][0] = 0
        var nVisited: Int
        do {
            nVisited = 0
            for (i in cornersDistance.indices) {
                for (j in 0 until cornersDistance[i].length) {
                    if (cornersDistance[i][j] == depth) {
                        for (k in moves2.indices) {
                            val nextCornerPermutation = cornersPermutationMove[i][k]
                            val nextEdgeCombination = edgesCombinationMove[j][k]
                            if (cornersDistance[nextCornerPermutation][nextEdgeCombination] < 0) {
                                cornersDistance[nextCornerPermutation][nextEdgeCombination] = (depth + 1).toByte()
                                nVisited++
                            }
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        edgesDistance = Array(N_EDGES_PERMUTATIONS) { ByteArray(N_CORNERS_COMBINATIONS) }
        for (i in edgesDistance.indices) {
            for (j in 0 until edgesDistance[i].length) {
                edgesDistance[i][j] = -1
            }
        }
        edgesDistance[0][0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in edgesDistance.indices) {
                for (j in 0 until edgesDistance[i].length) {
                    if (edgesDistance[i][j] == depth) {
                        for (k in moves2.indices) {
                            val nextEdgesPermutation = edgesPermutationMove[i][k]
                            val nextCornersCombination = cornersCombinationMove[j][k]
                            if (edgesDistance[nextEdgesPermutation][nextCornersCombination] < 0) {
                                edgesDistance[nextEdgesPermutation][nextCornersCombination] = (depth + 1).toByte()
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

    private fun isEvenPermutation(permutation: ByteArray): Boolean {
        var nInversions = 0
        for (i in permutation.indices) {
            for (j in i + 1 until permutation.size) {
                if (permutation[i] > permutation[j]) {
                    nInversions++
                }
            }
        }
        return nInversions % 2 == 0
    }

    fun solve(state: State): Array<String?> {
        val sequence = ArrayList<String>()
        var top = 0
        var bottom = 0
        for (moveIndex in solution(state)) {
            if (moveIndex < 11) {
                top += moveIndex + 1
                top %= 12
            } else if (moveIndex < 22) {
                bottom += moveIndex - 11 + 1
                bottom %= 12
            } else {
                if (top != 0 || bottom != 0) {
                    if (top > 6) {
                        top = -(12 - top)
                    }
                    if (bottom > 6) {
                        bottom = -(12 - bottom)
                    }
                    sequence.add(String.format("(%d,%d)", top, bottom))
                    top = 0
                    bottom = 0
                }
                sequence.add("/")
            }
        }
        if (top != 0 || bottom != 0) {
            if (top > 6) {
                top = -(12 - top)
            }
            if (bottom > 6) {
                bottom = -(12 - bottom)
            }
            sequence.add(String.format("(%d,%d)", top, bottom))
        }
        val sequenceArray = arrayOfNulls<String>(sequence.size)
        sequence.toArray(sequenceArray)
        return sequenceArray
    }

    fun generate(state: State): Array<String?> {
        val sequence = ArrayList<String>()
        var top = 0
        var bottom = 0
        val solution = solution(state)
        for (i in solution.indices.reversed()) {
            if (solution[i] < 11) {
                top += 12 - (solution[i] + 1)
                top %= 12
            } else if (solution[i] < 22) {
                bottom += 12 - (solution[i] - 11 + 1)
                bottom %= 12
            } else {
                if (top != 0 || bottom != 0) {
                    if (top > 6) {
                        top = -(12 - top)
                    }
                    if (bottom > 6) {
                        bottom = -(12 - bottom)
                    }
                    sequence.add(String.format("(%d,%d)", top, bottom))
                    top = 0
                    bottom = 0
                }
                sequence.add("/")
            }
        }
        if (top != 0 || bottom != 0) {
            if (top > 6) {
                top = -(12 - top)
            }
            if (bottom > 6) {
                bottom = -(12 - bottom)
            }
            sequence.add(String.format("(%d,%d)", top, bottom))
        }
        val sequenceArray = arrayOfNulls<String>(sequence.size)
        sequence.toArray(sequenceArray)
        return sequenceArray
    }

    private fun solution(state: State): IntArray {
        if (!initialized) {
            initialize()
        }
        var depth = 0
        while (true) {
            val solution1 = ArrayList<Int>()
            val solution2 = ArrayList<Int>()
            if (search(state, isEvenPermutation(state.piecesPermutation), depth, solution1, solution2)) {
                val sequence = ArrayList<Int>()
                for (moveIndex in solution1) {
                    sequence.add(moveIndex)
                }
                val phase2MoveMapping = arrayOf(intArrayOf(2), intArrayOf(5), intArrayOf(8), intArrayOf(13), intArrayOf(16), intArrayOf(19), intArrayOf(0, 22, 10), intArrayOf(21, 22, 11))
                for (moveIndex in solution2) {
                    for (phase1MoveIndex in phase2MoveMapping[moveIndex]) {
                        sequence.add(phase1MoveIndex)
                    }
                }
                val sequenceArray = IntArray(sequence.size)
                for (i in sequenceArray.indices) {
                    sequenceArray[i] = sequence[i]
                }
                return sequenceArray
            }
            depth++
        }
    }

    private fun search(state: State, isEvenPermutation: Boolean, depth: Int, solution1: ArrayList<Int>, solution2: ArrayList<Int>): Boolean {
        if (depth == 0) {
            if (isEvenPermutation && state.shapeIndex == State.id!!.shapeIndex) {
                val sequence2 = solution2(state.toCubeState(), 17)
                if (sequence2 != null) {
                    for (m in sequence2) {
                        solution2.add(m)
                    }
                    return true
                }
            }
            return false
        }
        val distance = if (isEvenPermutation) evenShapeDistance!![state.shapeIndex]!! else oddShapeDistance!![state.shapeIndex]!!
        if (distance <= depth) {
            for (i in moves1.indices) {
                if (i == 22 && !state.isTwistable) {
                    continue
                }
                val next = state.multiply(moves1[i])
                solution1.add(i)
                if (search(
                                next,
                                isEvenPermutation(next.piecesPermutation),
                                depth - 1,
                                solution1,
                                solution2)) {
                    return true
                }
                solution1.removeAt(solution1.size - 1)
            }
        }
        return false
    }

    private fun solution2(state: CubeState, maxDepth: Int): IntArray? {
        val cornersPermutation = IndexMapping.permutationToIndex(state.cornersPermutation)
        val isTopCorner = BooleanArray(8)
        for (k in isTopCorner.indices) {
            isTopCorner[k] = state.cornersPermutation!![k] < 4
        }
        val cornersCombination = IndexMapping.combinationToIndex(isTopCorner, 4)
        val edgesPermutation = IndexMapping.permutationToIndex(state.edgesPermutation)
        val isTopEdge = BooleanArray(8)
        for (k in isTopEdge.indices) {
            isTopEdge[k] = state.edgesPermutation!![k] < 4
        }
        val edgesCombination = IndexMapping.combinationToIndex(isTopEdge, 4)
        for (depth in 0..maxDepth) {
            val solution = IntArray(depth)
            if (search2(cornersPermutation, cornersCombination, edgesPermutation, edgesCombination, depth, solution)) {
                return solution
            }
        }
        return null
    }

    private fun search2(cornersPermutation: Int, cornersCombination: Int, edgesPermutation: Int, edgesCombination: Int, depth: Int, solution: IntArray): Boolean {
        if (depth == 0) {
            return cornersPermutation == 0 && edgesPermutation == 0
        }
        if (cornersDistance[cornersPermutation][edgesCombination] <= depth &&
                edgesDistance[edgesPermutation][cornersCombination] <= depth) {
            for (i in moves2.indices) {
                if (solution.size - depth - 1 >= 0 && solution[solution.size - depth - 1] / 3 == i / 3) {
                    continue
                }
                solution[solution.size - depth] = i
                if (search2(
                                cornersPermutationMove[cornersPermutation][i],
                                cornersCombinationMove[cornersCombination][i],
                                edgesPermutationMove[edgesPermutation][i],
                                edgesCombinationMove[edgesCombination][i],
                                depth - 1,
                                solution)) {
                    return true
                }
            }
        }
        return false
    }

    fun getRandomState(shape: State?, random: Random): State {
        val cornersPermutation = IndexMapping.indexToPermutation(
                random.nextInt(N_CORNERS_PERMUTATIONS), 8)
        val edgesPermutation = IndexMapping.indexToPermutation(
                random.nextInt(N_EDGES_PERMUTATIONS), 8)
        val permutation = ByteArray(shape!!.permutation.size)
        for (i in permutation.indices) {
            if (shape.permutation[i] < 8) {
                permutation[i] = cornersPermutation!![shape.permutation[i].toInt()]
            } else {
                permutation[i] = (8 + edgesPermutation!![shape.permutation[i] - 8]).toByte()
            }
        }
        return State(permutation)
    }

    fun getRandomState(random: Random): State {
        if (!initialized) {
            initialize()
        }
        return getRandomState(
                shapes!![random.nextInt(shapes!!.size)],
                random)
    }
}