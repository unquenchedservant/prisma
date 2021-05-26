package com.puzzletimer.state

import com.puzzletimer.timer.Timer.stop
import com.puzzletimer.timer.Timer.setInspectionEnabled
import com.puzzletimer.timer.Timer.start
import com.puzzletimer.timer.Timer.setSmoothTimingEnabled
import java.util.Arrays
import com.puzzletimer.models.ColorScheme
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import com.puzzletimer.models.ConfigurationEntry
import java.util.TimerTask
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.models.Scramble
import java.util.Collections
import com.puzzletimer.models.Solution
import java.util.UUID
import com.puzzletimer.models.Timing
import com.puzzletimer.state.UpdateManager
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import java.lang.StringBuilder
import kotlin.Throws
import java.io.IOException
import java.io.BufferedReader
import java.nio.charset.Charset
import java.util.ArrayList

class SessionManager {
    open class Listener {
        open fun solutionsUpdated(solutions: Array<Solution?>?) {}
        open fun dailySessionSet(hideTimerEnabledSet: Boolean) {}
    }

    private val listeners: ArrayList<Listener>
    private val solutions: HashMap<UUID, Solution>
    var isDailySessionEnabled = false
        set(dailySessionEnabled) {
            field = dailySessionEnabled
            for (listener in listeners) {
                listener.dailySessionSet(dailySessionEnabled)
            }
        }

    fun getSolutions(): Array<Solution?> {
        val solutions = ArrayList(solutions.values)
        Collections.sort(solutions) { solution1: Solution, solution2: Solution ->
            val start1 = solution1.timing.start
            val start2 = solution2.timing.start
            start2.compareTo(start1)
        }
        val solutionsArray = arrayOfNulls<Solution>(solutions.size)
        solutions.toArray(solutionsArray)
        return solutionsArray
    }

    fun addSolution(solution: Solution) {
        solutions[solution.solutionId] = solution
        notifyListeners()
    }

    fun updateSolution(solution: Solution) {
        if (solutions.containsKey(solution.solutionId)) {
            solutions[solution.solutionId] = solution
            notifyListeners()
        }
    }

    fun removeSolution(solution: Solution) {
        solutions.remove(solution.solutionId)
        notifyListeners()
    }

    fun clearSession() {
        solutions.clear()
        notifyListeners()
    }

    fun notifyListeners() {
        val solutions = ArrayList(solutions.values)
        Collections.sort(solutions) { solution1: Solution, solution2: Solution ->
            val start1 = solution1.timing.start
            val start2 = solution2.timing.start
            start2.compareTo(start1)
        }
        val solutionsArray = arrayOfNulls<Solution>(solutions.size)
        solutions.toArray(solutionsArray)
        for (listener in listeners) {
            listener.solutionsUpdated(solutionsArray)
        }
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    init {
        listeners = ArrayList()
        solutions = HashMap()
    }
}