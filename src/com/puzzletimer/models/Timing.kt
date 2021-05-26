package com.puzzletimer.models

import com.puzzletimer.util.StringUtils.join
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.models.Scramble
import com.puzzletimer.models.Timing
import com.puzzletimer.models.Solution
import java.util.*

class Timing(val start: Date, val end: Date?) {
    val elapsedTime: Long
        get() = if (end == null) Date().time - start.time else end.time - start.time
}