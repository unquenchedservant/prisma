package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class Average(override val minimumWindowSize: Int, override val maximumWindowSize: Int) : StatisticalMeasure {
    override var value: Long = 0
        private set
    override val windowPosition: Int
        get() = 0
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)

        // if number of DNFs is greater than one, return DNF
        var nDNFs = 0
        for (time in times) {
            if (time == Long.MAX_VALUE) {
                nDNFs++
                if (nDNFs > 1) {
                    value = Long.MAX_VALUE
                    return
                }
            }
        }
        var worst = Long.MIN_VALUE
        var best = Long.MAX_VALUE
        var sum = 0L
        for (time in times) {
            if (time > worst) {
                worst = time
            }
            if (time < best) {
                best = time
            }
            sum += time
        }
        value = (sum - worst - best) / (solutions.size - 2)
    }
}