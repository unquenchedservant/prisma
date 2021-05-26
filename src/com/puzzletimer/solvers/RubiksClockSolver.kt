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

object RubiksClockSolver {
    fun generate(state: State): Array<String?> {
        val inverseMatrix = arrayOf(intArrayOf(0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0), intArrayOf(0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0), intArrayOf(0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 1), intArrayOf(0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0), intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1, 0), intArrayOf(-1, 1, 0, 1, -1, 0, 0, 0, 0, -1, 0, 1, -1, 0), intArrayOf(0, 1, -1, 0, -1, 1, 0, 0, 0, -1, -1, 1, 0, 0), intArrayOf(0, 0, 0, 0, -1, 1, 0, 1, -1, 0, -1, 1, 0, -1), intArrayOf(0, 0, 0, 1, -1, 0, -1, 1, 0, 0, 0, 1, -1, -1), intArrayOf(1, -1, 1, -1, 1, -1, 1, -1, 1, 2, 2, -4, 2, 2), intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 3, -1, -1))
        val independentClocks = intArrayOf(
                0, 1, 2,
                3, 4, 5,
                6, 7, 8,
                10,
                12, 13, 14,
                17)
        val turns = IntArray(14)
        for (i in 0..13) {
            turns[i] = 0
            for (j in 0..13) {
                turns[i] += inverseMatrix[i][j] * state.clocks[independentClocks[j]]
            }
            while (turns[i] < -5 || turns[i] > 6) {
                if (turns[i] < 0) {
                    turns[i] += 12
                } else {
                    turns[i] -= 12
                }
            }
        }
        val sequence = arrayOfNulls<String>(10)
        sequence[0] = "UUdd " + "u=" + turns[0] + ",d=" + turns[1]
        sequence[1] = "dUdU " + "u=" + turns[2] + ",d=" + turns[3]
        sequence[2] = "ddUU " + "u=" + turns[4] + ",d=" + turns[5]
        sequence[3] = "UdUd " + "u=" + turns[6] + ",d=" + turns[7]
        sequence[4] = "dUUU " + "u=" + turns[8]
        sequence[5] = "UdUU " + "u=" + turns[9]
        sequence[6] = "UUUd " + "u=" + turns[10]
        sequence[7] = "UUdU " + "u=" + turns[11]
        sequence[8] = "UUUU " + "u=" + turns[12]
        sequence[9] = "dddd " + "d=" + turns[13]
        return sequence
    }

    fun getRandomSequence(r: Random): Array<String?> {
        val clocks = IntArray(18)
        for (i in clocks.indices) {
            clocks[i] = r.nextInt(12)
        }
        val generator = generate(State(clocks, null))
        var pins = ""
        for (i in 0..3) {
            pins += if (r.nextBoolean()) "U" else "d"
        }
        val sequence = arrayOfNulls<String>(generator.size + 1)
        System.arraycopy(generator, 0, sequence, 0, generator.size)
        sequence[sequence.size - 1] = pins
        return sequence
    }

    class State(var clocks: IntArray, var pinsDown: BooleanArray?) {
        fun rotateWheel(pinsDown: BooleanArray, wheel: Int, turns: Int): State {
            val newClocks = IntArray(18)
            System.arraycopy(clocks, 0, newClocks, 0, newClocks.size)

            // front
            val affectedClocks = BooleanArray(18)
            for (i in affectedClocks.indices) {
                affectedClocks[i] = false
            }
            if (pinsDown[wheel]) {
                for (i in 0..3) {
                    if (pinsDown[i]) {
                        affectedClocks[wheelsClockFront[i]] = true
                    }
                }
            } else {
                for (i in 0..3) {
                    if (!pinsDown[i]) {
                        for (clock in pinsClocksFront[i]) {
                            affectedClocks[clock] = true
                        }
                    }
                }
            }
            for (i in clocks.indices) {
                if (affectedClocks[i]) {
                    newClocks[i] = (newClocks[i] + 12 + turns) % 12
                }
            }

            // back
            for (i in affectedClocks.indices) {
                affectedClocks[i] = false
            }
            if (!pinsDown[wheel]) {
                for (i in 0..3) {
                    if (!pinsDown[i]) {
                        affectedClocks[wheelsClockBack[i]] = true
                    }
                }
            } else {
                for (i in 0..3) {
                    if (pinsDown[i]) {
                        for (clock in pinsClocksBack[i]) {
                            affectedClocks[clock] = true
                        }
                    }
                }
            }
            for (i in clocks.indices) {
                if (affectedClocks[i]) {
                    newClocks[i] = (newClocks[i] + 12 - turns) % 12
                }
            }
            return State(newClocks, pinsDown)
        }

        companion object {
            var wheelsClockFront: IntArray
            var wheelsClockBack: IntArray
            var pinsClocksFront: Array<IntArray>
            var pinsClocksBack: Array<IntArray>
            var id: State? = null

            init {
                wheelsClockFront = intArrayOf(
                        0, 2, 6, 8
                )
                wheelsClockBack = intArrayOf(
                        11, 9, 17, 15
                )
                pinsClocksFront = arrayOf(intArrayOf(0, 1, 3, 4), intArrayOf(1, 2, 4, 5), intArrayOf(3, 4, 6, 7), intArrayOf(4, 5, 7, 8))
                pinsClocksBack = arrayOf(intArrayOf(10, 11, 13, 14), intArrayOf(9, 10, 12, 13), intArrayOf(13, 14, 16, 17), intArrayOf(12, 13, 15, 16))
                id = State(intArrayOf(
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0), booleanArrayOf(
                        false, false,
                        false, false))
            }
        }
    }
}