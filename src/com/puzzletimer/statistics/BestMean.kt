package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class BestMean(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var windowPosition = 0
        private set
    override var value: Long = 0
        private set
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)
        var bestMean = Long.MAX_VALUE
        for (i in 0 until times.size - minimumWindowSize + 1) {
            var sum: Long = 0
            for (j in 0 until minimumWindowSize) {
                if (times[i + j] == Long.MAX_VALUE) {
                    sum = Long.MAX_VALUE
                    break
                }
                sum += times[i + j]
            }
            if (sum == Long.MAX_VALUE) {
                continue
            }
            val mean = sum / minimumWindowSize
            if (mean < bestMean) {
                bestMean = mean
                windowPosition = i
            }
        }
        value = bestMean
    }
}