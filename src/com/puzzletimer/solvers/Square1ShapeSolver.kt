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
import java.util.regex.Pattern

object Square1ShapeSolver {
    // constants
    const val N_POSITIONS = 16777216

    // distance table
    var distance: IntArray
    fun solve(state: State): Array<String?> {
        var state = state
        val sequence = ArrayList<String>()
        while (distance[state.index] > 0) {
            // twist
            if (state.isTwistable) {
                val next = state.twist()
                if (distance[next.index] == distance[state.index] - 1) {
                    sequence.add("/")
                    state = next
                }
            }

            // rotate top
            var x = 0
            var nextTop = State(state.index)
            for (i in 0..11) {
                if (distance[nextTop.index] == distance[state.index] - 1) {
                    x = i
                    state = nextTop
                    break
                }
                nextTop = nextTop.rotateTop()
            }

            // rotate bottom
            var y = 0
            var nextBottom = State(state.index)
            for (j in 0..11) {
                if (distance[nextBottom.index] == distance[state.index] - 1) {
                    y = j
                    state = nextBottom
                    break
                }
                nextBottom = nextBottom.rotateBottom()
            }
            if (x != 0 || y != 0) {
                sequence.add("(" + (if (x <= 6) x else x - 12) + "," + (if (y <= 6) y else y - 12) + ")")
            }
        }
        val sequenceArray = arrayOfNulls<String>(sequence.size)
        sequence.toArray(sequenceArray)
        return sequenceArray
    }

    class State {
        var index: Int

        constructor(index: Int) {
            this.index = index
        }

        constructor(cuts: BooleanArray) {
            index = 0

            // bottom
            for (i in 0..11) {
                index = index shl 1
                if (cuts[23 - i]) {
                    index = index or 1
                }
            }

            // top
            for (i in 0..11) {
                index = index shl 1
                if (cuts[11 - i]) {
                    index = index or 1
                }
            }
        }

        private val top: Int
            private get() = index and 0xFFF
        private val bottom: Int
            private get() = index shr 12 and 0xFFF

        fun rotateTop(): State {
            return State(bottom shl 12 or rotate(top))
        }

        fun rotateBottom(): State {
            return State(rotate(bottom) shl 12 or top)
        }

        fun twist(): State {
            val top = top
            val bottom = bottom
            val newTop = top and 0xF80 or (bottom and 0x7F)
            val newBottom = bottom and 0xF80 or (top and 0x7F)
            return State(newBottom shl 12 or newTop)
        }

        val isTwistable: Boolean
            get() {
                val top = top
                val bottom = bottom
                return top and (1 shl 0) != 0 && top and (1 shl 6) != 0 && bottom and (1 shl 0) != 0 && bottom and (1 shl 6) != 0
            }

        fun applyMove(move: String): State {
            var state = this
            if (move == "/") {
                state = state.twist()
            } else {
                val p = Pattern.compile("\\((-?\\d+),(-?\\d+)\\)")
                val matcher = p.matcher(move)
                matcher.find()
                val top = matcher.group(1).toInt()
                for (i in 0 until top + 12) {
                    state = state.rotateTop()
                }
                val bottom = matcher.group(2).toInt()
                for (i in 0 until bottom + 12) {
                    state = state.rotateBottom()
                }
            }
            return state
        }

        fun applySequence(sequence: Array<String>): State {
            var state = this
            for (move in sequence) {
                state = state.applyMove(move)
            }
            return state
        }

        companion object {
            private fun rotate(layer: Int): Int {
                return layer shl 1 and 0xFFE or (layer shr 11 and 1)
            }

            var id: State? = null

            init {
                id = State(booleanArrayOf(
                        true, false, true, true, false, true, true, false, true, true, false, true,
                        true, true, false, true, true, false, true, true, false, true, true, false))
            }
        }
    }

    init {
        distance = IntArray(N_POSITIONS)
        for (i in distance.indices) {
            distance[i] = -1
        }
        distance[State.id!!.index] = 0
        var nVisitedPositions: Int
        val depth = 0
        do {
            com.puzzletimer.solvers.nVisitedPositions = 0
            for (i in distance.indices) {
                if (distance[i] == com.puzzletimer.solvers.depth) {
                    val state = State(i)

                    // twist
                    if (com.puzzletimer.solvers.state.isTwistable()) {
                        val next: State = com.puzzletimer.solvers.state.twist()
                        if (distance[com.puzzletimer.solvers.next.index] == -1) {
                            distance[com.puzzletimer.solvers.next.index] = com.puzzletimer.solvers.depth + 1
                            com.puzzletimer.solvers.nVisitedPositions++
                        }
                    }

                    // rotate top
                    val nextTop = State(i)
                    for (j in 0..10) {
                        com.puzzletimer.solvers.nextTop = com.puzzletimer.solvers.nextTop.rotateTop()
                        if (distance[com.puzzletimer.solvers.nextTop.index] == -1) {
                            distance[com.puzzletimer.solvers.nextTop.index] = com.puzzletimer.solvers.depth + 1
                            com.puzzletimer.solvers.nVisitedPositions++
                        }
                    }

                    // rotate bottom
                    val nextBottom = State(i)
                    for (j in 0..10) {
                        com.puzzletimer.solvers.nextBottom = com.puzzletimer.solvers.nextBottom.rotateBottom()
                        if (distance[com.puzzletimer.solvers.nextBottom.index] == -1) {
                            distance[com.puzzletimer.solvers.nextBottom.index] = com.puzzletimer.solvers.depth + 1
                            com.puzzletimer.solvers.nVisitedPositions++
                        }
                    }
                }
            }
            com.puzzletimer.solvers.depth++
        } while (com.puzzletimer.solvers.nVisitedPositions > 0)
    }
}