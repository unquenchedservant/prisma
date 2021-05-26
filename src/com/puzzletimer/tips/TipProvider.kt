package com.puzzletimer.tips

import com.puzzletimer.tips.Tip
import com.puzzletimer.tips.RubiksCubeOptimalCross
import com.puzzletimer.tips.RubiksCubeOptimalXCross
import com.puzzletimer.tips.RubiksCube3OPCycles
import com.puzzletimer.tips.RubiksCubeClassicPochmannEdges
import com.puzzletimer.tips.RubiksCubeClassicPochmannCorners
import com.puzzletimer.tips.RubiksCubeM2Edges
import com.puzzletimer.tips.Square1OptimalCubeShapeTip

class TipProvider {
    val all: Array<Tip>
    operator fun get(tipId: String): Tip? {
        for (tip in all) {
            if (tip.tipId == tipId) {
                return tip
            }
        }
        return null
    }

    init {
        all = arrayOf(
                RubiksCubeOptimalCross(),
                RubiksCubeOptimalXCross(),
                RubiksCube3OPCycles(),
                RubiksCubeClassicPochmannEdges(),
                RubiksCubeClassicPochmannCorners(),
                RubiksCubeM2Edges(),
                Square1OptimalCubeShapeTip())
    }
}