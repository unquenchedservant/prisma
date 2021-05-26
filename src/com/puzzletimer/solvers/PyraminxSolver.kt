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

class PyraminxSolver(private val minScrambleLength: Int) {
    class State(
            var tipsOrientation: ByteArray?,
            var verticesOrientation: ByteArray?,
            var edgesPermutation: ByteArray?,
            var edgesOrientation: ByteArray?) {
        fun multiply(move: State?): State {
            val tipsOrientation = ByteArray(4)
            for (i in 0..3) {
                tipsOrientation[i] = ((this.tipsOrientation!![i] + move!!.tipsOrientation!![i]) % 3).toByte()
            }
            val verticesOrientation = ByteArray(4)
            for (i in 0..3) {
                verticesOrientation[i] = ((this.verticesOrientation!![i] + move!!.verticesOrientation!![i]) % 3).toByte()
            }
            val edgesPermutation = ByteArray(6)
            val edgesOrientation = ByteArray(6)
            for (i in 0..5) {
                edgesPermutation[i] = this.edgesPermutation!![move!!.edgesPermutation!![i].toInt()]
                edgesOrientation[i] = ((this.edgesOrientation!![move.edgesPermutation!![i].toInt()] + move.edgesOrientation!![i]) % 2).toByte()
            }
            return State(tipsOrientation, verticesOrientation, edgesPermutation, edgesOrientation)
        }
    }

    val N_TIPS_ORIENTATIONS = 81
    val N_VERTICES_ORIENTATIONS = 81
    val N_EDGES_PERMUTATIONS = 360
    val N_EDGES_ORIENTATIONS = 32
    private var initialized = false
    private var tipMoves: Array<State>
    private var tipMoveNames: Array<String>
    private var moves: Array<State>
    private var moveNames: Array<String>
    private var tipsOrientationMove: Array<IntArray>
    private var verticesOrientationMove: Array<IntArray>
    private var edgesPermutationMove: Array<IntArray>
    private var edgesOrientationMove: Array<IntArray>
    private var tipsOrientationDistance: ByteArray
    private var verticesOrientationDistance: ByteArray
    private var edgesPermutationDistance: ByteArray
    private var edgesOrientationDistance: ByteArray
    private fun initialize() {
        val moveu = State(byteArrayOf(1, 0, 0, 0), byteArrayOf(0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5), byteArrayOf(0, 0, 0, 0, 0, 0))
        val movel = State(byteArrayOf(0, 1, 0, 0), byteArrayOf(0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5), byteArrayOf(0, 0, 0, 0, 0, 0))
        val mover = State(byteArrayOf(0, 0, 1, 0), byteArrayOf(0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5), byteArrayOf(0, 0, 0, 0, 0, 0))
        val moveb = State(byteArrayOf(0, 0, 0, 1), byteArrayOf(0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5), byteArrayOf(0, 0, 0, 0, 0, 0))
        tipMoves = arrayOf(
                moveu,
                moveu.multiply(moveu),
                movel,
                movel.multiply(movel),
                mover,
                mover.multiply(mover),
                moveb,
                moveb.multiply(moveb))
        tipMoveNames = arrayOf(
                "u", "u'",
                "l", "l'",
                "r", "r'",
                "b", "b'")
        val moveU = State(byteArrayOf(1, 0, 0, 0), byteArrayOf(1, 0, 0, 0), byteArrayOf(2, 0, 1, 3, 4, 5), byteArrayOf(0, 0, 0, 0, 0, 0))
        val moveL = State(byteArrayOf(0, 1, 0, 0), byteArrayOf(0, 1, 0, 0), byteArrayOf(0, 1, 5, 3, 2, 4), byteArrayOf(0, 0, 1, 0, 0, 1))
        val moveR = State(byteArrayOf(0, 0, 1, 0), byteArrayOf(0, 0, 1, 0), byteArrayOf(0, 4, 2, 1, 3, 5), byteArrayOf(0, 1, 0, 0, 1, 0))
        val moveB = State(byteArrayOf(0, 0, 0, 1), byteArrayOf(0, 0, 0, 1), byteArrayOf(3, 1, 2, 5, 4, 0), byteArrayOf(1, 0, 0, 1, 0, 0))
        moves = arrayOf(
                moveU,
                moveU.multiply(moveU),
                moveL,
                moveL.multiply(moveL),
                moveR,
                moveR.multiply(moveR),
                moveB,
                moveB.multiply(moveB))
        moveNames = arrayOf(
                "U", "U'",
                "L", "L'",
                "R", "R'",
                "B", "B'")

        // move tables
        tipsOrientationMove = Array(N_TIPS_ORIENTATIONS) { IntArray(tipMoveNames.size) }
        for (i in tipsOrientationMove.indices) {
            val state = State(IndexMapping.indexToOrientation(i, 3, 4), ByteArray(4), ByteArray(6), ByteArray(6))
            for (j in moves.indices) {
                tipsOrientationMove[i][j] = IndexMapping.orientationToIndex(state.multiply(tipMoves[j]).tipsOrientation, 3)
            }
        }
        verticesOrientationMove = Array(N_VERTICES_ORIENTATIONS) { IntArray(moves.size) }
        for (i in verticesOrientationMove.indices) {
            val state = State(ByteArray(4), IndexMapping.indexToOrientation(i, 3, 4), ByteArray(6), ByteArray(6))
            for (j in moves.indices) {
                verticesOrientationMove[i][j] = IndexMapping.orientationToIndex(state.multiply(moves[j]).verticesOrientation, 3)
            }
        }
        edgesPermutationMove = Array(N_EDGES_PERMUTATIONS) { IntArray(moves.size) }
        for (i in edgesPermutationMove.indices) {
            val state = State(ByteArray(4), ByteArray(4), IndexMapping.indexToEvenPermutation(i, 6), ByteArray(6))
            for (j in moves.indices) {
                edgesPermutationMove[i][j] = IndexMapping.evenPermutationToIndex(state.multiply(moves[j]).edgesPermutation)
            }
        }
        edgesOrientationMove = Array(N_EDGES_ORIENTATIONS) { IntArray(moves.size) }
        for (i in edgesOrientationMove.indices) {
            val state = State(ByteArray(4), ByteArray(4), ByteArray(6), IndexMapping.indexToZeroSumOrientation(i, 2, 6))
            for (j in moves.indices) {
                edgesOrientationMove[i][j] = IndexMapping.zeroSumOrientationToIndex(state.multiply(moves[j]).edgesOrientation, 2)
            }
        }

        // prune tables
        tipsOrientationDistance = ByteArray(N_TIPS_ORIENTATIONS)
        for (i in tipsOrientationDistance.indices) {
            tipsOrientationDistance[i] = -1
        }
        tipsOrientationDistance[0] = 0
        var depth = 0
        var nVisited: Int
        do {
            nVisited = 0
            for (i in tipsOrientationDistance.indices) {
                if (tipsOrientationDistance[i] == depth) {
                    for (k in tipMoves.indices) {
                        val next = tipsOrientationMove[i][k]
                        if (tipsOrientationDistance[next] < 0) {
                            tipsOrientationDistance[next] = (depth + 1).toByte()
                            nVisited++
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        verticesOrientationDistance = ByteArray(N_VERTICES_ORIENTATIONS)
        for (i in verticesOrientationDistance.indices) {
            verticesOrientationDistance[i] = -1
        }
        verticesOrientationDistance[0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in verticesOrientationDistance.indices) {
                if (verticesOrientationDistance[i] == depth) {
                    for (k in moves.indices) {
                        val next = verticesOrientationMove[i][k]
                        if (verticesOrientationDistance[next] < 0) {
                            verticesOrientationDistance[next] = (depth + 1).toByte()
                            nVisited++
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        edgesPermutationDistance = ByteArray(N_EDGES_PERMUTATIONS)
        for (i in edgesPermutationDistance.indices) {
            edgesPermutationDistance[i] = -1
        }
        edgesPermutationDistance[0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in edgesPermutationDistance.indices) {
                if (edgesPermutationDistance[i] == depth) {
                    for (k in moves.indices) {
                        val next = edgesPermutationMove[i][k]
                        if (edgesPermutationDistance[next] < 0) {
                            edgesPermutationDistance[next] = (depth + 1).toByte()
                            nVisited++
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        edgesOrientationDistance = ByteArray(N_EDGES_ORIENTATIONS)
        for (i in edgesOrientationDistance.indices) {
            edgesOrientationDistance[i] = -1
        }
        edgesOrientationDistance[0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in edgesOrientationDistance.indices) {
                if (edgesOrientationDistance[i] == depth) {
                    for (k in moves.indices) {
                        val next = edgesOrientationMove[i][k]
                        if (edgesOrientationDistance[next] < 0) {
                            edgesOrientationDistance[next] = (depth + 1).toByte()
                            nVisited++
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        initialized = true
    }

    private fun solveTips(state: State): Array<String?> {
        if (!initialized) {
            initialize()
        }
        val tipsOrientation = IndexMapping.orientationToIndex(state.tipsOrientation, 3)
        var depth = 0
        while (true) {
            val solution = ArrayList<String>()
            if (searchTips(tipsOrientation, depth, solution, -1)) {
                val sequence = arrayOfNulls<String>(solution.size)
                solution.toArray(sequence)
                return sequence
            }
            depth++
        }
    }

    private fun searchTips(tipsOrientation: Int, depth: Int, solution: ArrayList<String>, lastVertex: Int): Boolean {
        if (depth == 0) {
            return tipsOrientation == 0
        }
        if (tipsOrientationDistance[tipsOrientation] <= depth) {
            for (i in tipMoves.indices) {
                if (i / 2 == lastVertex) {
                    continue
                }
                solution.add(tipMoveNames[i])
                if (searchTips(
                                tipsOrientationMove[tipsOrientation][i],
                                depth - 1,
                                solution,
                                i / 2)) {
                    return true
                }
                solution.removeAt(solution.size - 1)
            }
        }
        return false
    }

    private fun solve(state: State): Array<String?> {
        if (!initialized) {
            initialize()
        }
        val verticesOrientation = IndexMapping.orientationToIndex(state.verticesOrientation, 3)
        val edgesPermutation = IndexMapping.evenPermutationToIndex(state.edgesPermutation)
        val edgesOrientation = IndexMapping.zeroSumOrientationToIndex(state.edgesOrientation, 2)
        var depth = minScrambleLength
        while (true) {
            val solution = ArrayList<String>()
            if (search(verticesOrientation, edgesPermutation, edgesOrientation, depth, solution, -1)) {
                val sequence = arrayOfNulls<String>(solution.size)
                solution.toArray(sequence)
                return sequence
            }
            depth++
        }
    }

    private fun search(
            verticesOrientation: Int,
            edgesPermutation: Int,
            edgesOrientation: Int,
            depth: Int,
            solution: ArrayList<String>,
            lastVertex: Int): Boolean {
        if (depth == 0) {
            return verticesOrientation == 0 && edgesPermutation == 0 && edgesOrientation == 0
        }
        if (verticesOrientationDistance[verticesOrientation] <= depth && edgesPermutationDistance[edgesPermutation] <= depth && edgesOrientationDistance[edgesOrientation] <= depth) {
            for (i in moves.indices) {
                if (i / 2 == lastVertex) {
                    continue
                }
                solution.add(moveNames[i])
                if (search(
                                verticesOrientationMove[verticesOrientation][i],
                                edgesPermutationMove[edgesPermutation][i],
                                edgesOrientationMove[edgesOrientation][i],
                                depth - 1,
                                solution,
                                i / 2)) {
                    return true
                }
                solution.removeAt(solution.size - 1)
            }
        }
        return false
    }

    fun generate(state: State): Array<String?> {
        var state = state
        val inverseMoveNames = HashMap<String?, String>()
        inverseMoveNames["u"] = "u'"
        inverseMoveNames["u'"] = "u"
        inverseMoveNames["l"] = "l'"
        inverseMoveNames["l'"] = "l"
        inverseMoveNames["r"] = "r'"
        inverseMoveNames["r'"] = "r"
        inverseMoveNames["b"] = "b'"
        inverseMoveNames["b'"] = "b"
        inverseMoveNames["U"] = "U'"
        inverseMoveNames["U'"] = "U"
        inverseMoveNames["L"] = "L'"
        inverseMoveNames["L'"] = "L"
        inverseMoveNames["R"] = "R'"
        inverseMoveNames["R'"] = "R"
        inverseMoveNames["B"] = "B'"
        inverseMoveNames["B'"] = "B"
        val solution = solve(state)
        val moves = HashMap<String?, State>()
        moves["U"] = this.moves[0]
        moves["U'"] = this.moves[1]
        moves["L"] = this.moves[2]
        moves["L'"] = this.moves[3]
        moves["R"] = this.moves[4]
        moves["R'"] = this.moves[5]
        moves["B"] = this.moves[6]
        moves["B'"] = this.moves[7]
        for (move in solution) {
            state = state.multiply(moves[move])
        }
        val tipsSolution = solveTips(state)
        val sequence = arrayOfNulls<String>(tipsSolution.size + solution.size)
        for (i in solution.indices) {
            sequence[i] = inverseMoveNames[solution[solution.size - 1 - i]]
        }
        for (i in tipsSolution.indices) {
            sequence[solution.size + i] = inverseMoveNames[tipsSolution[tipsSolution.size - 1 - i]]
        }
        return sequence
    }

    fun getRandomState(random: Random): State {
        val tipsOrientation = random.nextInt(N_TIPS_ORIENTATIONS)
        val verticesOrientation = random.nextInt(N_VERTICES_ORIENTATIONS)
        val edgesPermutation = random.nextInt(N_EDGES_PERMUTATIONS)
        val edgesOrientation = random.nextInt(N_EDGES_ORIENTATIONS)
        return State(
                IndexMapping.indexToOrientation(tipsOrientation, 3, 4),
                IndexMapping.indexToOrientation(verticesOrientation, 3, 4),
                IndexMapping.indexToEvenPermutation(edgesPermutation, 6),
                IndexMapping.indexToZeroSumOrientation(edgesOrientation, 2, 6))
    }
}