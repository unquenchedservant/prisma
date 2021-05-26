package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class Mean(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var value: Long = 0
        private set
    override val windowPosition: Int
        get() = 0
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)
        var sum = 0L
        for (time in times) {
            if (time == Long.MAX_VALUE) {
                value = Long.MAX_VALUE
                return
            }
            sum += time
        }
        value = sum / solutions.size
    }
}