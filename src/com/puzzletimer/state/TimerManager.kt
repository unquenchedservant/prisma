package com.puzzletimer.state

import com.puzzletimer.timer.Timer.stop
import com.puzzletimer.timer.Timer.setInspectionEnabled
import com.puzzletimer.timer.Timer.start
import com.puzzletimer.timer.Timer.setSmoothTimingEnabled
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.models.ColorScheme.FaceColor
import com.puzzletimer.models.ConfigurationEntry
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.models.Scramble
import com.puzzletimer.models.Solution
import com.puzzletimer.models.Timing
import com.puzzletimer.state.UpdateManager
import com.puzzletimer.timer.Timer
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import java.lang.StringBuilder
import kotlin.Throws
import java.io.IOException
import java.io.BufferedReader
import java.nio.charset.Charset
import java.util.*

class TimerManager {
    open class Listener {
        // timer
        open fun timerChanged(timer: Timer?) {}
        open fun timerReset() {}
        open fun precisionChanged(timerPrecisionId: String?) {}
        open fun smoothTimingSet(smoothTimingEnabled: Boolean) {}

        // any key
        open fun anyKeyEnabledSet(anyKeyEnabledSet: Boolean) {}

        // hide timer
        open fun hideTimerEnabledSet(hideTimerEnabledSet: Boolean) {}

        // hands
        open fun leftHandPressed() {}
        open fun leftHandReleased() {}
        open fun rightHandPressed() {}
        open fun rightHandReleased() {}

        // inspection
        open fun inspectionEnabledSet(inspectionEnabled: Boolean) {}
        open fun inspectionStarted() {}
        open fun inspectionRunning(remainingTime: Long) {}
        open fun inspectionFinished() {}

        // solution
        open fun solutionStarted() {}
        open fun solutionRunning(timing: Timing?, hidden: Boolean) {}
        open fun solutionFinished(timing: Timing?, penalty: String?) {}

        // stackmat
        fun dataReceived(data: ByteArray?, offset: Int, period: Double, decodedBits: ByteArray?, decodedBytes: ByteArray?) {}
        open fun dataNotReceived(data: ByteArray?) {}
    }

    private val listeners: ArrayList<Listener>
    private var currentTimer: Timer?
    private var inspectionEnabled: Boolean
    private var anyKeyEnabled: Boolean
    private var hideTimerEnabled: Boolean
    private var smoothTimingEnabled: Boolean
    private var repeater: java.util.Timer?
    private var inspectionStart: Date?
    private var penalty: String

    // timer
    fun setTimer(timer: Timer?) {
        // suspend running inspection
        if (inspectionStart != null) {
            repeater!!.cancel()
            inspectionStart = null
            penalty = ""
        }
        if (currentTimer != null) {
            currentTimer!!.stop()
        }
        currentTimer = timer
        currentTimer!!.setInspectionEnabled(inspectionEnabled)
        for (listener in listeners) {
            listener.timerChanged(timer)
        }
        currentTimer!!.start()
    }

    fun resetTimer() {
        for (listener in listeners) {
            listener.timerReset()
        }
    }

    fun setPrecision(timerPrecisionId: String?) {
        for (listener in listeners) {
            listener.precisionChanged(timerPrecisionId)
        }
    }

    // inspection
    fun isSmoothTimingEnabled(): Boolean {
        return smoothTimingEnabled
    }

    fun setSmoothTimingEnabled(smoothTimingEnabled: Boolean) {
        this.smoothTimingEnabled = smoothTimingEnabled
        if (currentTimer != null) {
            currentTimer!!.setSmoothTimingEnabled(smoothTimingEnabled)
        }
        for (listener in listeners) {
            listener.smoothTimingSet(smoothTimingEnabled)
        }
    }

    // any key
    fun isAnyKeyEnabled(): Boolean {
        return anyKeyEnabled
    }

    fun setAnyKeyEnabled(anyKeyEnabled: Boolean) {
        this.anyKeyEnabled = anyKeyEnabled
        for (listener in listeners) {
            listener.anyKeyEnabledSet(anyKeyEnabled)
        }
    }

    // hide timer
    fun isHideTimerEnabled(): Boolean {
        return hideTimerEnabled
    }

    fun setHideTimerEnabled(hideTimerEnabled: Boolean) {
        this.hideTimerEnabled = hideTimerEnabled
        for (listener in listeners) {
            listener.hideTimerEnabledSet(hideTimerEnabled)
        }
    }

    // hands
    fun pressLeftHand() {
        for (listener in listeners) {
            listener.leftHandPressed()
        }
    }

    fun releaseLeftHand() {
        for (listener in listeners) {
            listener.leftHandReleased()
        }
    }

    fun pressRightHand() {
        for (listener in listeners) {
            listener.rightHandPressed()
        }
    }

    fun releaseRightHand() {
        for (listener in listeners) {
            listener.rightHandReleased()
        }
    }

    // inspection
    fun isInspectionEnabled(): Boolean {
        return inspectionEnabled
    }

    fun setInspectionEnabled(inspectionEnabled: Boolean) {
        this.inspectionEnabled = inspectionEnabled
        if (currentTimer != null) {
            currentTimer!!.setInspectionEnabled(inspectionEnabled)
        }
        for (listener in listeners) {
            listener.inspectionEnabledSet(inspectionEnabled)
        }
    }

    fun startInspection() {
        for (listener in listeners) {
            listener.inspectionStarted()
        }
        inspectionStart = Date()
        penalty = ""
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val start = inspectionStart!!.time
                val now = Date().time
                for (listener in listeners) {
                    listener.inspectionRunning(15000 - (now - start))
                }
                if (now - start > 17000) {
                    repeater!!.cancel()
                    for (listener in listeners) {
                        listener.inspectionFinished()
                    }
                    inspectionStart = null
                    penalty = "DNF"
                    finishSolution(Timing(Date(now), Date(now)))
                } else if (now - start > 15000) {
                    penalty = "+2"
                }
            }
        }
        repeater = Timer()
        repeater!!.schedule(timerTask, 0, 10)
    }

    // solution
    fun startSolution() {
        if (inspectionStart != null) {
            repeater!!.cancel()
            inspectionStart = null
            for (listener in listeners) {
                listener.inspectionFinished()
            }
        }
        for (listener in listeners) {
            listener.solutionStarted()
        }
    }

    fun updateSolutionTiming(timing: Timing?) {
        for (listener in listeners) {
            listener.solutionRunning(timing, isHideTimerEnabled())
        }
    }

    fun finishSolution(timing: Timing?) {
        for (listener in listeners) {
            listener.solutionFinished(timing, penalty)
        }
        penalty = ""
    }

    fun dataReceived(data: ByteArray?, offset: Int, period: Double, decodedBits: ByteArray?, decodedBytes: ByteArray?) {
        for (listener in listeners) {
            listener.dataReceived(data, offset, period, decodedBits, decodedBytes)
        }
    }

    fun dataNotReceived(data: ByteArray?) {
        for (listener in listeners) {
            listener.dataNotReceived(data)
        }
    }

    // listeners
    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    init {
        listeners = ArrayList()
        currentTimer = null
        inspectionEnabled = false
        anyKeyEnabled = false
        hideTimerEnabled = false
        smoothTimingEnabled = true
        repeater = null
        inspectionStart = null
        penalty = ""
    }
}