package com.puzzletimer.models

import com.puzzletimer.util.StringUtils.join
import java.util.UUID
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.models.Scramble
import com.puzzletimer.models.Timing
import com.puzzletimer.models.Solution

class PuzzleInfo(val puzzleId: String) {
    val description: String
        get() = _("puzzle." + puzzleId)

    override fun toString(): String {
        return description
    }
}