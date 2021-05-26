package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class InterquartileMean(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var value: Long = 0
        private set
    override val windowPosition: Int
        get() = 0
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)
        val lowerQuartile = Percentile(1, Int.MAX_VALUE, 0.25)
        lowerQuartile.setSolutions(solutions, round)
        val upperQuartile = Percentile(1, Int.MAX_VALUE, 0.75)
        upperQuartile.setSolutions(solutions, round)
        var sum = 0L
        var nTimes = 0
        for (time in times) {
            if (time < lowerQuartile.value || time > upperQuartile.value) {
                continue
            }
            if (time == Long.MAX_VALUE) {
                value = Long.MAX_VALUE
                return
            }
            sum += time
            nTimes++
        }
        value = sum / nTimes
    }
}