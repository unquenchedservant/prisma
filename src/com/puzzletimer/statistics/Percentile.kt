package com.puzzletimer.statistics

import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.models.Solution
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.statistics.Percentile
import java.util.Arrays

class Percentile(override val minimumWindowSize: Int, override val maximumWindowSize: Int, private val p: Double) : StatisticalMeasure {
    override var value: Long = 0
        private set
    override val windowPosition: Int
        get() = 0
    override val round: Boolean
        get() = true

    override fun setSolutions(solutions: Array<Solution?>, round: Boolean) {
        val times = realTimes(solutions, false, round)
        Arrays.sort(times)
        val position = p * (solutions.size + 1)
        if (position < 1.0) {
            value = times[0]
            return
        } else if (position >= times.size) {
            value = times[times.size - 1]
            return
        }
        val index = Math.floor(position).toInt()
        if (times[index - 1] == Long.MAX_VALUE || position - index != 0.0 && times[index] == Long.MAX_VALUE) {
            value = Long.MAX_VALUE
            return
        }
        value = (times[index - 1] + (position - index) * (times[index] - times[index - 1])).toLong()
    }
}