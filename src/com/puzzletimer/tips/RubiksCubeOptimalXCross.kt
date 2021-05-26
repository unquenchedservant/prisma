package com.puzzletimer.tips

import com.puzzletimer.util.StringUtils.join
import com.puzzletimer.tips.Tip
import com.puzzletimer.solvers.RubiksCubeSolver
import com.puzzletimer.tips.RubiksCubeOptimalXCross
import com.puzzletimer.models.Scramble
import java.lang.StringBuilder
import com.puzzletimer.solvers.RubiksCubeXCrossSolver
import java.util.ArrayList

class RubiksCubeOptimalXCross : Tip {
    companion object {
        private var x: RubiksCubeSolver.State? = null
        private var y: RubiksCubeSolver.State? = null
        private var z: RubiksCubeSolver.State? = null

        init {
            x = RubiksCubeSolver.State(byteArrayOf(3, 2, 6, 7, 0, 1, 5, 4), byteArrayOf(2, 1, 2, 1, 1, 2, 1, 2), byteArrayOf(7, 5, 9, 11, 6, 2, 10, 3, 4, 1, 8, 0), byteArrayOf(0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0))
            y = RubiksCubeSolver.State(byteArrayOf(3, 0, 1, 2, 7, 4, 5, 6), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(3, 0, 1, 2, 7, 4, 5, 6, 11, 8, 9, 10), byteArrayOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0))
            z = RubiksCubeSolver.State(byteArrayOf(4, 0, 3, 7, 5, 1, 2, 6), byteArrayOf(1, 2, 1, 2, 2, 1, 2, 1), byteArrayOf(8, 4, 6, 10, 0, 7, 3, 11, 1, 5, 2, 9), byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1))
        }
    }

    override val tipId: String
        get() = "RUBIKS-CUBE-OPTIMAL-X-CROSS"
    override val puzzleId: String
        get() = "RUBIKS-CUBE"
    override val tipDescription: String
        get() = _("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS")

    override fun getTip(scramble: Scramble?): String? {
        val state = RubiksCubeSolver.State.id.applySequence(scramble!!.sequence)
        val tip = StringBuilder()

        // x-cross on U
        val stateU = x!!.multiply(x).multiply(state).multiply(x).multiply(x)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS.optimal_x_cross_on_u")).append(":\n")
        tip.append(getOptimalSolutions(stateU, "x2 "))
        tip.append("\n")

        // x-cross on D
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS.optimal_x_cross_on_d")).append(":\n")
        tip.append(getOptimalSolutions(state, ""))
        tip.append("\n")

        // x-cross on L
        val stateL = z!!.multiply(state).multiply(z).multiply(z).multiply(z)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS.optimal_x_cross_on_l")).append(":\n")
        tip.append(getOptimalSolutions(stateL, "z' "))
        tip.append("\n")

        // x-cross on R
        val stateR = z!!.multiply(z).multiply(z).multiply(state).multiply(z)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS.optimal_x_cross_on_r")).append(":\n")
        tip.append(getOptimalSolutions(stateR, "z "))
        tip.append("\n")

        // x-cross on F
        val stateF = x!!.multiply(state).multiply(x).multiply(x).multiply(x)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS.optimal_x_cross_on_f")).append(":\n")
        tip.append(getOptimalSolutions(stateF, "x' "))
        tip.append("\n")

        // x-cross on B
        val stateB = x!!.multiply(x).multiply(x).multiply(state).multiply(x)
        tip.append(_("tip.RUBIKS-CUBE-OPTIMAL-X-CROSS.optimal_x_cross_on_b")).append(":\n")
        tip.append(getOptimalSolutions(stateB, "x "))
        tip.append("\n")
        return tip.toString().trim { it <= ' ' }
    }

    private fun getOptimalSolutions(state: RubiksCubeSolver.State, prefix: String): String {
        val prefixes = ArrayList<String>()
        val solutions = ArrayList<Array<String?>>()

        // id
        for (solution in RubiksCubeXCrossSolver.solve(state)) {
            prefixes.add(prefix)
            solutions.add(solution)
        }

        // y
        val stateY = y!!.multiply(y).multiply(y).multiply(state).multiply(y)
        for (solution in RubiksCubeXCrossSolver.solve(stateY)) {
            prefixes.add(prefix + "y ")
            solutions.add(solution)
        }

        // y2
        val stateY2 = y!!.multiply(y).multiply(state).multiply(y).multiply(y)
        for (solution in RubiksCubeXCrossSolver.solve(stateY2)) {
            prefixes.add(prefix + "y2 ")
            solutions.add(solution)
        }

        // y'
        val stateY3 = y!!.multiply(state).multiply(y).multiply(y).multiply(y)
        for (solution in RubiksCubeXCrossSolver.solve(stateY3)) {
            prefixes.add(prefix + "y' ")
            solutions.add(solution)
        }
        var minLength = Int.MAX_VALUE
        for (solution in solutions) {
            if (solution.size < minLength) {
                minLength = solution.size
            }
        }
        val output = StringBuilder()
        for (i in solutions.indices) {
            if (solutions[i].length == minLength) {
                output.append(String.format("  %s%s\n",
                        prefixes[i],
                        join(" ", solutions[i])))
            }
        }
        return output.toString()
    }

    override fun toString(): String {
        return tipDescription
    }
}