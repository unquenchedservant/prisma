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

class SkewbSolver {
    class State(var facesPermutation: ByteArray?, var freeCornersPermutation: ByteArray?, var freeCornersOrientation: ByteArray?,
                var fixedCornersOrientation: ByteArray?) {
        fun multiply(move: State): State {
            // faces permutation
            val facesPermutation = ByteArray(6)
            for (i in facesPermutation.indices) {
                facesPermutation[i] = this.facesPermutation!![move.facesPermutation!![i].toInt()]
            }

            // free corners permutation
            val freeCornersPermutation = ByteArray(4)
            for (i in freeCornersPermutation.indices) {
                freeCornersPermutation[i] = this.freeCornersPermutation!![move.freeCornersPermutation!![i].toInt()]
            }

            // free corners orientation
            val freeCornersOrientation = ByteArray(4)
            for (i in freeCornersOrientation.indices) {
                freeCornersOrientation[i] = ((this.freeCornersOrientation!![move.freeCornersPermutation!![i].toInt()] +
                        move.freeCornersOrientation!![i]) % 3).toByte()
            }

            // fixed corners orientation
            val fixedCornersOrientation = ByteArray(4)
            for (i in freeCornersOrientation.indices) {
                fixedCornersOrientation[i] = ((this.fixedCornersOrientation!![i] +
                        move.fixedCornersOrientation!![i]) % 3).toByte()
            }
            return State(
                    facesPermutation,
                    freeCornersPermutation,
                    freeCornersOrientation,
                    fixedCornersOrientation)
        }
    }

    private val N_FACES_PERMUTATIONS = 360
    private val N_FREE_CORNERS_PERMUTATION = 12
    private val N_FREE_CORNERS_ORIENTATION = 27
    private val N_FIXED_CORNERS_ORIENTATION = 81
    private var initialized = false
    private var moves: Array<State>
    private var facesPermutationMove: Array<IntArray>
    private var freeCornersPermutationMove: Array<IntArray>
    private var freeCornersOrientationMove: Array<IntArray>
    private var fixedCornersOrientationMove: Array<IntArray>
    private var distance: Array<Array<Array<IntArray>>>
    private fun initialize() {
        // moves
        val moveL = State(byteArrayOf(1, 4, 2, 3, 0, 5), byteArrayOf(2, 0, 1, 3), byteArrayOf(2, 2, 2, 0), byteArrayOf(1, 0, 0, 0))
        val moveR = State(byteArrayOf(3, 1, 0, 2, 4, 5), byteArrayOf(1, 3, 2, 0), byteArrayOf(2, 2, 0, 2), byteArrayOf(0, 1, 0, 0))
        val moveD = State(byteArrayOf(0, 1, 2, 4, 5, 3), byteArrayOf(0, 2, 3, 1), byteArrayOf(0, 2, 2, 2), byteArrayOf(0, 0, 0, 1))
        val moveB = State(byteArrayOf(0, 2, 5, 3, 4, 1), byteArrayOf(3, 1, 0, 2), byteArrayOf(2, 0, 2, 2), byteArrayOf(0, 0, 1, 0))
        moves = arrayOf(
                moveL,
                moveL.multiply(moveL),
                moveR,
                moveR.multiply(moveR),
                moveD,
                moveD.multiply(moveD),
                moveB,
                moveB.multiply(moveB))

        // move tables
        facesPermutationMove = Array(N_FACES_PERMUTATIONS) { IntArray(moves.size) }
        for (i in facesPermutationMove.indices) {
            val state = State(
                    IndexMapping.indexToEvenPermutation(i, 6), ByteArray(4), ByteArray(4), ByteArray(4))
            for (j in moves.indices) {
                facesPermutationMove[i][j] = IndexMapping.evenPermutationToIndex(
                        state.multiply(moves[j]).facesPermutation)
            }
        }
        freeCornersPermutationMove = Array(N_FREE_CORNERS_PERMUTATION) { IntArray(moves.size) }
        for (i in freeCornersPermutationMove.indices) {
            val state = State(ByteArray(6),
                    IndexMapping.indexToEvenPermutation(i, 4), ByteArray(4), ByteArray(4))
            for (j in moves.indices) {
                freeCornersPermutationMove[i][j] = IndexMapping.evenPermutationToIndex(
                        state.multiply(moves[j]).freeCornersPermutation)
            }
        }
        freeCornersOrientationMove = Array(N_FREE_CORNERS_ORIENTATION) { IntArray(moves.size) }
        for (i in freeCornersOrientationMove.indices) {
            val state = State(ByteArray(6), ByteArray(4),
                    IndexMapping.indexToZeroSumOrientation(i, 3, 4), ByteArray(4))
            for (j in moves.indices) {
                freeCornersOrientationMove[i][j] = IndexMapping.zeroSumOrientationToIndex(
                        state.multiply(moves[j]).freeCornersOrientation, 3)
            }
        }
        fixedCornersOrientationMove = Array(N_FIXED_CORNERS_ORIENTATION) { IntArray(moves.size) }
        for (i in fixedCornersOrientationMove.indices) {
            val state = State(ByteArray(6), ByteArray(4), ByteArray(4),
                    IndexMapping.indexToOrientation(i, 3, 4))
            for (j in moves.indices) {
                fixedCornersOrientationMove[i][j] = IndexMapping.orientationToIndex(
                        state.multiply(moves[j]).fixedCornersOrientation, 3)
            }
        }

        // distance table
        distance = Array(N_FACES_PERMUTATIONS) { Array(N_FREE_CORNERS_PERMUTATION) { Array(N_FREE_CORNERS_ORIENTATION) { IntArray(N_FIXED_CORNERS_ORIENTATION) } } }
        for (i in distance.indices) {
            for (j in 0 until distance[i].length) {
                for (k in 0 until distance[i][j].length) {
                    for (m in 0 until distance[i][j][k].length) {
                        distance[i][j][k][m] = -1
                    }
                }
            }
        }
        distance[0][0][0][0] = 0
        var nVisited: Int
        var depth = 0
        do {
            nVisited = 0
            for (i in distance.indices) {
                for (j in 0 until distance[i].length) {
                    for (k in 0 until distance[i][j].length) {
                        for (m in 0 until distance[i][j][k].length) {
                            if (distance[i][j][k][m] == depth) {
                                for (moveIndex in moves.indices) {
                                    val nextFacesPermutation = facesPermutationMove[i][moveIndex]
                                    val nextFreeCornersPemutation = freeCornersPermutationMove[j][moveIndex]
                                    val nextFreeCornersOrientation = freeCornersOrientationMove[k][moveIndex]
                                    val nextFixedCornersOrientation = fixedCornersOrientationMove[m][moveIndex]
                                    if (distance[nextFacesPermutation][nextFreeCornersPemutation][nextFreeCornersOrientation][nextFixedCornersOrientation] == -1) {
                                        distance[nextFacesPermutation][nextFreeCornersPemutation][nextFreeCornersOrientation][nextFixedCornersOrientation] = depth + 1
                                        nVisited++
                                    }
                                }
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
        val moveNames = arrayOf("L", "L'", "R", "R'", "D", "D'", "B", "B'")
        val sequence = ArrayList<String>()
        var facesPermutation = IndexMapping.evenPermutationToIndex(
                state.facesPermutation)
        var freeCornersPermutation = IndexMapping.evenPermutationToIndex(
                state.freeCornersPermutation)
        var freeCornersOrientation = IndexMapping.zeroSumOrientationToIndex(
                state.freeCornersOrientation, 3)
        var fixedCornersOrientation = IndexMapping.orientationToIndex(
                state.fixedCornersOrientation, 3)
        while (true) {
            if (distance[facesPermutation][freeCornersPermutation][freeCornersOrientation][fixedCornersOrientation] == 0) {
                break
            }
            for (k in moves.indices) {
                val nextFacesPermutation = facesPermutationMove[facesPermutation][k]
                val nextFreeCornersPemutation = freeCornersPermutationMove[freeCornersPermutation][k]
                val nextFreeCornersOrientation = freeCornersOrientationMove[freeCornersOrientation][k]
                val nextFixedCornersOrientation = fixedCornersOrientationMove[fixedCornersOrientation][k]
                if (distance[nextFacesPermutation][nextFreeCornersPemutation][nextFreeCornersOrientation][nextFixedCornersOrientation] ==
                        distance[facesPermutation][freeCornersPermutation][freeCornersOrientation][fixedCornersOrientation] - 1) {
                    sequence.add(moveNames[k])
                    facesPermutation = nextFacesPermutation
                    freeCornersPermutation = nextFreeCornersPemutation
                    freeCornersOrientation = nextFreeCornersOrientation
                    fixedCornersOrientation = nextFixedCornersOrientation
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
        inverseMoves["L"] = "L'"
        inverseMoves["L'"] = "L"
        inverseMoves["R"] = "R'"
        inverseMoves["R'"] = "R"
        inverseMoves["D"] = "D'"
        inverseMoves["D'"] = "D"
        inverseMoves["B"] = "B'"
        inverseMoves["B'"] = "B"
        val sequence = arrayOfNulls<String>(solution.size)
        for (i in sequence.indices) {
            sequence[i] = inverseMoves[solution[solution.size - 1 - i]]
        }
        return sequence
    }

    fun getRandomState(random: Random): State {
        if (!initialized) {
            initialize()
        }
        while (true) {
            val indexFacesPermutation = random.nextInt(N_FACES_PERMUTATIONS)
            val indexFreeCornersPermutation = random.nextInt(N_FREE_CORNERS_PERMUTATION)
            val indexFreeCornersOrientation = random.nextInt(N_FREE_CORNERS_ORIENTATION)
            val indexFixedCornersOrientation = random.nextInt(N_FIXED_CORNERS_ORIENTATION)
            if (distance[indexFacesPermutation][indexFreeCornersPermutation][indexFreeCornersOrientation][indexFixedCornersOrientation] == -1) {
                continue
            }
            return State(
                    IndexMapping.indexToEvenPermutation(indexFacesPermutation, 6),
                    IndexMapping.indexToEvenPermutation(indexFreeCornersPermutation, 4),
                    IndexMapping.indexToZeroSumOrientation(indexFreeCornersOrientation, 3, 4),
                    IndexMapping.indexToOrientation(indexFixedCornersOrientation, 3, 4))
        }
    }
}