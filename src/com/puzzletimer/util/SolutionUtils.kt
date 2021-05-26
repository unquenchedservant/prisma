package com.puzzletimer.util

import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.models.Solution
import java.util.*

object SolutionUtils {
    fun formatSeconds(time: Long, timerPrecisionId: String, round: Boolean): String {
        var time = time
        var result = ""
        if (time == Long.MAX_VALUE) {
            return "DNF"
        }
        var sign = ""
        if (time < 0) {
            sign = "-"
            time = -time
        }
        if (timerPrecisionId == "CENTISECONDS") {
            if (round) {
                time = time + 5
            }
            time = time / 10
            val seconds = time / 100
            val centiseconds = time % 100
            result = sign +
                    seconds + "." +
                    if (centiseconds < 10) "0$centiseconds" else centiseconds
        } else if (timerPrecisionId == "MILLISECONDS") {
            val seconds = time / 1000
            val milliseconds = time % 1000
            result = sign +
                    seconds + "." +
                    if (milliseconds < 10) "00$milliseconds" else if (milliseconds < 100) "0$milliseconds" else milliseconds
        }
        return result
    }

    fun formatMinutes(time: Long, timerPrecisionId: String, round: Boolean): String {
        var time = time
        var result = ""
        if (time == Long.MAX_VALUE) {
            return "DNF"
        }
        var sign = ""
        if (time < 0) {
            sign = "-"
            time = -time
        }
        if (timerPrecisionId == "CENTISECONDS") {
            if (round) {
                time = time + 5
            }
            time = time / 10
            val minutes = time / 6000
            val seconds = time / 100 % 60
            val centiseconds = time % 100
            result = sign +
                    (if (minutes < 10) "0$minutes" else minutes) + ":" +
                    (if (seconds < 10) "0$seconds" else seconds) + "." +
                    if (centiseconds < 10) "0$centiseconds" else centiseconds
        } else if (timerPrecisionId == "MILLISECONDS") {
            val minutes = time / 60000
            val seconds = time / 1000 % 60
            val milliseconds = time % 1000
            result = sign +
                    (if (minutes < 10) "0$minutes" else minutes) + ":" +
                    (if (seconds < 10) "0$seconds" else seconds) + "." +
                    if (milliseconds < 10) "00$milliseconds" else if (milliseconds < 100) "0$milliseconds" else milliseconds
        }
        return result
    }

    fun format(time: Long, timerPrecisionId: String, round: Boolean): String {
        var time = time
        if (-60000 < time && time < 60000) {
            return formatSeconds(time, timerPrecisionId, round)
        }
        var result = ""
        if (time == Long.MAX_VALUE) {
            return "DNF"
        }
        var sign = ""
        if (time < 0) {
            sign = "-"
            time = -time
        }
        if (timerPrecisionId == "CENTISECONDS") {
            if (round) {
                time = time + 5
            }
            time = time / 10
            val minutes = time / 6000
            val seconds = time / 100 % 60
            val centiseconds = time % 100
            result = sign +
                    minutes + ":" +
                    (if (seconds < 10) "0$seconds" else seconds) + "." +
                    if (centiseconds < 10) "0$centiseconds" else centiseconds
        } else if (timerPrecisionId == "MILLISECONDS") {
            val minutes = time / 60000
            val seconds = time / 1000 % 60
            val milliseconds = time % 1000
            result = sign +
                    minutes + ":" +
                    (if (seconds < 10) "0$seconds" else seconds) + "." +
                    if (milliseconds < 10) "00$milliseconds" else if (milliseconds < 100) "0$milliseconds" else milliseconds
        }
        return result
    }

    fun parseTime(input: String): Long {
        val scanner = Scanner(input.trim { it <= ' ' })
        scanner.useLocale(Locale.ENGLISH)
        val time: Long

        // 00:00.000
        time = if (input.contains(":")) {
            scanner.useDelimiter(":")
            if (!scanner.hasNextLong()) {
                return 0
            }
            val minutes = scanner.nextLong()
            if (minutes < 0) {
                return 0
            }
            if (!scanner.hasNextDouble()) {
                return 0
            }
            val seconds = scanner.nextDouble()
            if (seconds < 0.0 || seconds >= 60.0) {
                return 0
            }
            (60000 * minutes + 1000 * seconds).toLong()
        } else {
            if (!scanner.hasNextDouble()) {
                return 0
            }
            val seconds = scanner.nextDouble()
            if (seconds < 0.0) {
                return 0
            }
            (1000 * seconds).toLong()
        }
        return time
    }

    fun realTime(solution: Solution, round: Boolean): Long {
        if (solution.penalty == "DNF") {
            return Long.MAX_VALUE
        }
        if (solution.penalty == "+2" && round) {
            return (solution.timing.elapsedTime + 2000) / 10 * 10
        }
        if (solution.penalty == "+2") {
            return solution.timing.elapsedTime + 2000
        }
        return if (round) {
            solution.timing.elapsedTime / 10 * 10
        } else solution.timing.elapsedTime
    }

    fun realTimes(solutions: Array<Solution>, filterDNF: Boolean, round: Boolean): LongArray {
        val realTimes = ArrayList<Long>()
        for (i in solutions.indices) {
            val actualTime = realTime(solutions[i], round)
            if (!filterDNF || actualTime != Long.MAX_VALUE) {
                realTimes.add(actualTime)
            }
        }
        val realTimesArray = LongArray(realTimes.size)
        for (i in realTimesArray.indices) {
            realTimesArray[i] = realTimes[i]
        }
        return realTimesArray
    }
}