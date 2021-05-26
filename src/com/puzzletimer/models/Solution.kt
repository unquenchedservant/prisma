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

class Solution(val solutionId: UUID, val categoryId: UUID, val scramble: Scramble, val timing: Timing, val penalty: String, val comment: String) {
    fun setTiming(timing: Timing): Solution {
        return Solution(
                solutionId,
                categoryId,
                scramble,
                timing,
                penalty,
                comment)
    }

    fun setPenalty(penalty: String): Solution {
        return Solution(
                solutionId,
                categoryId,
                scramble,
                timing,
                penalty,
                comment)
    }

    fun setComment(comment: String): Solution {
        return Solution(
                solutionId,
                categoryId,
                scramble,
                timing,
                penalty,
                comment)
    }
}