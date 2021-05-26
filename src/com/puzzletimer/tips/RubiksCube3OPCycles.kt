package com.puzzletimer.tips

import com.puzzletimer.tips.Tip
import com.puzzletimer.models.Scramble
import java.lang.StringBuilder
import com.puzzletimer.solvers.RubiksCubeSolver
import java.util.ArrayList

class RubiksCube3OPCycles : Tip {
    override val tipId: String
        get() = "RUBIKS-CUBE-3OP-CYCLES"
    override val puzzleId: String
        get() = "RUBIKS-CUBE"
    override val tipDescription: String
        get() = _("tip.RUBIKS-CUBE-3OP-CYCLES")

    override fun getTip(scramble: Scramble?): String? {
        val tip = StringBuilder()
        tip.append(_("tip.RUBIKS-CUBE-3OP-CYCLES")).append(":\n  ")
        val cubeState = RubiksCubeSolver.State.id.applySequence(scramble!!.sequence)

        // corner cycles
        val cornersOrder = intArrayOf(3, 2, 1, 0, 7, 6, 5, 4)
        for (cycle in cycles(cornersOrder, cubeState.cornersPermutation)) {
            if (cycle.size < 2) {
                continue
            }
            val cornerNames = arrayOf(
                    "UBL", "UBR", "UFR", "UFL",
                    "DBL", "DBR", "DFR", "DFL")
            tip.append("(").append(cornerNames[cycle[0].toInt()])
            for (i in 1 until cycle.size) {
                tip.append(" ").append(cornerNames[cycle[i].toInt()])
            }
            tip.append(")")
        }
        tip.append("\n  ")

        // edges cycles
        val edgesOrder = intArrayOf(6, 7, 4, 5, 3, 0, 1, 2, 10, 11, 8, 9)
        for (cycle in cycles(edgesOrder, cubeState.edgesPermutation)) {
            if (cycle.size < 2) {
                continue
            }
            val edgeNames = arrayOf(
                    "BL", "BR", "FR", "FL",
                    "UB", "UR", "UF", "UL",
                    "DB", "DR", "DF", "DL")
            tip.append("(").append(edgeNames[cycle[0].toInt()])
            for (i in 1 until cycle.size) {
                tip.append(" ").append(edgeNames[cycle[i].toInt()])
            }
            tip.append(")")
        }
        tip.append("\n\n")
        return tip.toString().trim { it <= ' ' }
    }

    private fun cycles(order: IntArray, permutation: ByteArray): ArrayList<ArrayList<Byte>> {
        val visited = BooleanArray(permutation.size)
        for (i in visited.indices) {
            visited[i] = false
        }
        val cycles = ArrayList<ArrayList<Byte>>()
        for (i in permutation.indices) {
            if (visited[order[i]]) {
                continue
            }
            val cycle = ArrayList<Byte>()
            var current = order[i].toByte()
            do {
                cycle.add(current)
                visited[current.toInt()] = true
                current = permutation[current.toInt()]
            } while (current != cycle[0])
            cycles.add(cycle)
        }
        return cycles
    }

    override fun toString(): String {
        return tipDescription
    }
}