package com.puzzletimer.tips

import com.puzzletimer.util.StringUtils.join
import com.puzzletimer.tips.Tip
import com.puzzletimer.models.Scramble
import com.puzzletimer.solvers.Square1ShapeSolver

class Square1OptimalCubeShapeTip : Tip {
    override val tipId: String
        get() = "SQUARE-1-OPTIMAL-CUBE-SHAPE"
    override val puzzleId: String
        get() = "SQUARE-1"
    override val tipDescription: String
        get() = _("tip.SQUARE-1-OPTIMAL-CUBE-SHAPE")

    override fun getTip(scramble: Scramble?): String? {
        val solution = Square1ShapeSolver.solve(
                Square1ShapeSolver.State.id.applySequence(scramble!!.sequence))
        return _("tip.SQUARE-1-OPTIMAL-CUBE-SHAPE").toString() + ":\n  " + join(" ", solution)
    }

    override fun toString(): String {
        return tipDescription
    }
}