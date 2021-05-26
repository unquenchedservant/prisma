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

class RubiksPocketCubeSolver(private val minScrambleLength: Int, generatingSet: Array<String>) {
    class State(var permutation: ByteArray?, var orientation: ByteArray?) {
        fun multiply(move: State?): State {
            val resultPermutation = ByteArray(8)
            val resultOrientation = ByteArray(8)
            for (i in 0..7) {
                resultPermutation[i] = permutation!![move!!.permutation!![i].toInt()]
                resultOrientation[i] = ((orientation!![move.permutation!![i].toInt()] + move.orientation!![i]) % 3).toByte()
            }
            return State(resultPermutation, resultOrientation)
        }

        companion object {
            var id: State? = null

            init {
                id = State(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
            }
        }
    }

    private val N_PERMUTATIONS = 40320
    private val N_ORIENTATIONS = 2187
    private val moves: ArrayList<State?>
    private val moveNames: ArrayList<String>
    private var initialized: Boolean
    private var permutationMove: Array<IntArray>
    private var orientationMove: Array<IntArray>
    private var permutationDistance: ByteArray
    private var orientationDistance: ByteArray
    private fun initialize() {
        // move tables
        permutationMove = Array(N_PERMUTATIONS) { IntArray(moves.size) }
        for (i in permutationMove.indices) {
            val state = State(IndexMapping.indexToPermutation(i, 8), ByteArray(8))
            for (j in moves.indices) {
                permutationMove[i][j] = IndexMapping.permutationToIndex(
                        state.multiply(moves[j]).permutation)
            }
        }
        orientationMove = Array(N_ORIENTATIONS) { IntArray(moves.size) }
        for (i in orientationMove.indices) {
            val state = State(ByteArray(8), IndexMapping.indexToZeroSumOrientation(i, 3, 8))
            for (j in moves.indices) {
                orientationMove[i][j] = IndexMapping.zeroSumOrientationToIndex(
                        state.multiply(moves[j]).orientation, 3)
            }
        }

        // prune tables
        permutationDistance = ByteArray(N_PERMUTATIONS)
        for (i in permutationDistance.indices) {
            permutationDistance[i] = -1
        }
        permutationDistance[0] = 0
        var nVisited: Int
        var depth = 0
        do {
            nVisited = 0
            for (i in permutationDistance.indices) {
                if (permutationDistance[i] == depth) {
                    for (j in moves.indices) {
                        val next = permutationMove[i][j]
                        if (permutationDistance[next] < 0) {
                            permutationDistance[next] = (depth + 1).toByte()
                            nVisited++
                        }
                    }
                }
            }
            depth++
        } while (nVisited > 0)
        orientationDistance = ByteArray(N_ORIENTATIONS)
        for (i in orientationDistance.indices) {
            orientationDistance[i] = -1
        }
        orientationDistance[0] = 0
        depth = 0
        do {
            nVisited = 0
            for (i in orientationDistance.indices) {
                if (orientationDistance[i] == depth) {
                    for (j in moves.indices) {
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
        initialized = true
    }

    fun solve(state: State): Array<String?> {
        if (!initialized) {
            initialize()
        }
        val permutation = IndexMapping.permutationToIndex(state.permutation)
        val orientation = IndexMapping.zeroSumOrientationToIndex(state.orientation, 3)
        var depth = minScrambleLength
        while (true) {
            val solution = ArrayList<String>()
            if (search(permutation, orientation, depth, solution, -1)) {
                val sequence = arrayOfNulls<String>(solution.size)
                solution.toArray(sequence)
                return sequence
            }
            depth++
        }
    }

    private fun search(permutation: Int, orientation: Int, depth: Int, solution: ArrayList<String>, lastFace: Int): Boolean {
        if (depth == 0) {
            return permutation == 0 && orientation == 0
        }
        if (permutationDistance[permutation] <= depth &&
                orientationDistance[orientation] <= depth) {
            for (i in moves.indices) {
                if (i / 3 == lastFace) {
                    continue
                }
                solution.add(moveNames[i])
                if (search(
                                permutationMove[permutation][i],
                                orientationMove[orientation][i],
                                depth - 1,
                                solution,
                                i / 3)) {
                    return true
                }
                solution.removeAt(solution.size - 1)
            }
        }
        return false
    }

    fun generate(state: State): Array<String?> {
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
        val solution = solve(state)
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in sequence.indices) {
            sequence[i] = inverseMoveNames[solution[solution.size - 1 - i]]
        }
        return sequence
    }

    fun getRandomState(random: Random): State? {
        var state = State.id
        for (i in 0..99) {
            state = state!!.multiply(moves[random.nextInt(moves.size)])
        }
        return state
    }

    init {
        val table = HashMap<String, State>()
        table["U"] = State(byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
        table["D"] = State(byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
        table["L"] = State(byteArrayOf(4, 1, 2, 0, 7, 5, 6, 3), byteArrayOf(2, 0, 0, 1, 1, 0, 0, 2))
        table["R"] = State(byteArrayOf(0, 2, 6, 3, 4, 1, 5, 7), byteArrayOf(0, 1, 2, 0, 0, 2, 1, 0))
        table["F"] = State(byteArrayOf(0, 1, 3, 7, 4, 5, 2, 6), byteArrayOf(0, 0, 1, 2, 0, 0, 2, 1))
        table["B"] = State(byteArrayOf(1, 5, 2, 3, 0, 4, 6, 7), byteArrayOf(1, 2, 0, 0, 2, 1, 0, 0))
        moves = ArrayList()
        moveNames = ArrayList()
        for (moveName in generatingSet) {
            val move = table[moveName]
            moves.add(move)
            moveNames.add(moveName)
            moves.add(move!!.multiply(move))
            moveNames.add(moveName + "2")
            moves.add(move.multiply(move).multiply(move))
            moveNames.add("$moveName'")
        }
        initialized = false
    }
}