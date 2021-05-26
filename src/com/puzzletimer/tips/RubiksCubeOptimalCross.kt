package com.puzzletimer.tips

import com.puzzletimer.util.StringUtils.join
import com.puzzletimer.tips.Tip
import com.puzzletimer.solvers.RubiksCubeSolver
import com.puzzletimer.tips.RubiksCubeOptimalCross
import com.puzzletimer.models.Scramble
import java.lang.StringBuilder
import com.puzzletimer.solvers.RubiksCubeCrossSolver

class RubiksCubeOptimalCross : Tip {
    companion object {
        private var x: RubiksCubeSolver.State? = null
        private var z: RubiksCubeSolver.State? = null

        init {
            x = RubiksCubeSolver.State(byteArrayOf(3, 2, 6, 7, 0, 1, 5, 4), byteArrayOf(2, 1, 2, 1, 1, 2, 1, 2), byteArrayOf(7, 5, 9, 11, 6, 2, 10, 3, 4, 1, 8, 0), byteArrayOf(0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0))
            z = RubiksCubeSolver.State(byteArrayOf(4, 0, 3, 7, 5, 1, 2, 6), byteArrayOf(1, 2, 1, 2, 2, 1, 2, 1), byteArrayOf(8, 4, 6, 10, 0, 7, 3, 11, 1, 5, 2, 9), byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1))
        }
    }

    override val tipId: String
        get() = "RUBIKS-CUBE-OPTIMAL-CROSS"
    override val puzzleId: String
        get() = "RUBIKS-CUBE"
    override val tipDescription: String
        get() = _("tip.RUBIKS-CUBE-OPTIMAL-CROSS")

    override fun getTip(scramble: Scramble?): String? {
        val state = RubiksCubeSolver.State.id.applySequence(scramble!!.sequence)
        val tip = StringBuilder()

        // cross on U
        val stateU = x!!.multiply(x).multiply(state).multiply(x).multiply(x)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-CROSS.optimal_cross_on_u")).append(":\n")
        for (solution in RubiksCubeCrossSolver.solve(stateU)) {
            tip.append("  x2 ").append(join(" ", solution!!)).append("\n")
        }
        tip.append("\n")

        // cross on D
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-CROSS.optimal_cross_on_d")).append(":\n")
        for (solution in RubiksCubeCrossSolver.solve(state)) {
            tip.append("  ").append(join(" ", solution!!)).append("\n")
        }
        tip.append("\n")

        // cross on L
        val stateL = z!!.multiply(state).multiply(z).multiply(z).multiply(z)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-CROSS.optimal_cross_on_l")).append(":\n")
        for (solution in RubiksCubeCrossSolver.solve(stateL)) {
            tip.append("  z' ").append(join(" ", solution!!)).append("\n")
        }
        tip.append("\n")

        // cross on R
        val stateR = z!!.multiply(z).multiply(z).multiply(state).multiply(z)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-CROSS.optimal_cross_on_r")).append(":\n")
        for (solution in RubiksCubeCrossSolver.solve(stateR)) {
            tip.append("  z ").append(join(" ", solution!!)).append("\n")
        }
        tip.append("\n")

        // cross on F
        val stateF = x!!.multiply(state).multiply(x).multiply(x).multiply(x)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-CROSS.optimal_cross_on_f")).append(":\n")
        for (solution in RubiksCubeCrossSolver.solve(stateF)) {
            tip.append("  x' ").append(join(" ", solution!!)).append("\n")
        }
        tip.append("\n")

        // cross on B
        val stateB = x!!.multiply(x).multiply(x).multiply(state).multiply(x)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-CROSS.optimal_cross_on_b")).append(":\n")
        for (solution in RubiksCubeCrossSolver.solve(stateB)) {
            tip.append("  x ").append(join(" ", solution!!)).append("\n")
        }
        tip.append("\n")
        return tip.toString().trim { it <= ' ' }
    }

    override fun toString(): String {
        return tipDescription
    }
}