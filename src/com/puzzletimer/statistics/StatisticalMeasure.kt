package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

interface StatisticalMeasure {
    val minimumWindowSize: Int
    val maximumWindowSize: Int
    val windowPosition: Int
    val value: Long
    val round: Boolean
    fun setSolutions(solutions: Array<Solution?>, round: Boolean)
}