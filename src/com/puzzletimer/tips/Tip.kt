package com.puzzletimer.tips

import com.puzzletimer.models.Scramble

interface Tip {
    val tipId: String?
    val puzzleId: String?
    val tipDescription: String?
    fun getTip(scramble: Scramble?): String?
}