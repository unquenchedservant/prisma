package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class BestAverage(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var windowPosition = 0
        private set
    override var value: Long = 0
        private set
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)
        var bestAverage = Long.MAX_VALUE
        for (i in 0 until times.size - minimumWindowSize + 1) {
            var worst = Long.MIN_VALUE
            var best = Long.MAX_VALUE
            var sum: Long = 0
            for (j in 0 until minimumWindowSize) {
                if (times[i + j] == Long.MAX_VALUE && worst == Long.MAX_VALUE) {
                    sum = Long.MAX_VALUE
                    break
                }
                if (times[i + j] > worst) {
                    worst = times[i + j]
                }
                if (times[i + j] < best) {
                    best = times[i + j]
                }
                sum += times[i + j]
            }
            if (sum == Long.MAX_VALUE) {
                continue
            }
            val average = (sum - worst - best) / (minimumWindowSize - 2)
            if (average < bestAverage) {
                bestAverage = average
                windowPosition = i
            }
        }
        value = bestAverage
    }
}