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

object FloppyCubeSolver {
    private const val N_CORNERS_PERMUTATION = 24
    private const val N_EDGES_ORIENTATION = 16
    private var distance: Array<IntArray>
    fun solve(state: State): Array<String?> {
        var state = state
        val sequence = ArrayList<String>()
        while (true) {
            val cornersPermutationIndex = IndexMapping.permutationToIndex(state.cornersPermutation)
            val edgesOrientationIndex = IndexMapping.orientationToIndex(state.edgesOrientation, 2)
            if (distance[cornersPermutationIndex][edgesOrientationIndex] == 0) {
                break
            }
            for (move in arrayOf("U", "D", "L", "R")) {
                val nextState = state.multiply(State.moves!![move])
                val nextCornersPermutationIndex = IndexMapping.permutationToIndex(nextState.cornersPermutation)
                val nextEdgesOrientationIndex = IndexMapping.orientationToIndex(nextState.edgesOrientation, 2)
                if (distance[nextCornersPermutationIndex][nextEdgesOrientationIndex] ==
                        distance[cornersPermutationIndex][edgesOrientationIndex] - 1) {
                    sequence.add(move)
                    state = nextState
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
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in sequence.indices) {
            sequence[i] = solution[solution.size - 1 - i]
        }
        return sequence
    }

    fun getRandomState(random: Random): State {
        while (true) {
            val cornersPermutationIndex = random.nextInt(N_CORNERS_PERMUTATION)
            val edgesOrientationIndex = random.nextInt(N_EDGES_ORIENTATION)
            if (distance[cornersPermutationIndex][edgesOrientationIndex] >= 0) {
                val cornersPermutation = IndexMapping.indexToPermutation(cornersPermutationIndex, 4)
                val edgesOrientation = IndexMapping.indexToOrientation(edgesOrientationIndex, 2, 4)
                return State(cornersPermutation, edgesOrientation)
            }
        }
    }

    class State(var cornersPermutation: ByteArray?, var edgesOrientation: ByteArray?) {
        fun multiply(move: State?): State {
            // corners
            val cornersPermutation = ByteArray(4)
            for (i in 0..3) {
                cornersPermutation[i] = this.cornersPermutation!![move!!.cornersPermutation!![i].toInt()]
            }

            // edges
            val edgesOrientation = ByteArray(4)
            for (i in 0..3) {
                edgesOrientation[i] = ((this.edgesOrientation!![i] + move!!.edgesOrientation!![i]) % 2).toByte()
            }
            return State(cornersPermutation, edgesOrientation)
        }

        companion object {
            var moves: HashMap<String, State>? = null

            init {
                moves = HashMap()
                moves!!["U"] = State(byteArrayOf(1, 0, 2, 3), byteArrayOf(1, 0, 0, 0))
                moves!!["R"] = State(byteArrayOf(0, 2, 1, 3), byteArrayOf(0, 1, 0, 0))
                moves!!["D"] = State(byteArrayOf(0, 1, 3, 2), byteArrayOf(0, 0, 1, 0))
                moves!!["L"] = State(byteArrayOf(3, 1, 2, 0), byteArrayOf(0, 0, 0, 1))
            }
        }
    }

    init {
        distance = Array(N_CORNERS_PERMUTATION) { IntArray(N_EDGES_ORIENTATION) }
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
            for (i in 0 until N_CORNERS_PERMUTATION) {
                for (j in 0 until N_EDGES_ORIENTATION) {
                    if (distance[i][j] == com.puzzletimer.solvers.depth) {
                        val state = State(
                                IndexMapping.indexToPermutation(i, 4),
                                IndexMapping.indexToOrientation(j, 2, 4))
                        for (move in arrayOf("U", "R", "D", "L")) {
                            val newState: State = com.puzzletimer.solvers.state.multiply(State.moves!![com.puzzletimer.solvers.move])
                            val cornersPermutationIndex = IndexMapping.permutationToIndex(com.puzzletimer.solvers.newState.cornersPermutation)
                            val edgesOrientationIndex = IndexMapping.orientationToIndex(com.puzzletimer.solvers.newState.edgesOrientation, 2)
                            if (distance[com.puzzletimer.solvers.cornersPermutationIndex][com.puzzletimer.solvers.edgesOrientationIndex] == -1) {
                                distance[com.puzzletimer.solvers.cornersPermutationIndex][com.puzzletimer.solvers.edgesOrientationIndex] = com.puzzletimer.solvers.depth + 1
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