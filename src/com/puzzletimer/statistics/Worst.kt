package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class Worst(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var windowPosition = 0
        private set
    override var value: Long = 0
        private set
    override val round: Boolean
        get() = false

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)
        var worst = 0L
        for (i in times.indices) {
            if (times[i] >= worst) {
                worst = times[i]
                windowPosition = i
            }
        }
        value = worst
    }
}