package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class StandardDeviation(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var value: Long = 0
        private set
    override val windowPosition: Int
        get() = 0
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, true, round)
        if (times.size == 0) {
            value = 0L
            return
        }
        var mean = 0.0
        for (time in times) {
            mean += time.toDouble()
        }
        mean /= times.size.toDouble()
        var variance = 0.0
        for (time in times) {
            variance += Math.pow(time - mean, 2.0)
        }
        variance /= times.size.toDouble()
        value = Math.sqrt(variance).toLong()
    }
}