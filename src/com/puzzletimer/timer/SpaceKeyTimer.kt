package com.puzzletimer.timer

import javax.swing.JFrame
import com.puzzletimer.state.TimerManager
import java.awt.event.KeyListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import com.puzzletimer.models.Timing
import java.util.*

class SpaceKeyTimer(private val frame: JFrame, private val timerManager: TimerManager) : Timer {
    private enum class State {
        READY_FOR_INSPECTION, READY, RUNNING, FINISHED
    }

    private var inspectionEnabled = false
    private var keyListener: KeyListener? = null
    private var timerListener: TimerManager.Listener? = null
    private var repeater: java.util.Timer? = null
    private var start: Date? = null
    private var finish: Date
    private var state: State
    override fun getTimerId(): String {
        return "KEYBOARD-TIMER-SPACE"
    }

    override fun setInspectionEnabled(inspectionEnabled: Boolean) {
        this.inspectionEnabled = inspectionEnabled
        when (state) {
            State.READY_FOR_INSPECTION -> if (!inspectionEnabled) {
                state = State.READY
            }
            State.READY -> if (inspectionEnabled) {
                state = State.READY_FOR_INSPECTION
            }
        }
    }

    override fun start() {
        keyListener = object : KeyAdapter() {
            override fun keyPressed(keyEvent: KeyEvent) {
                if (keyEvent.keyCode != KeyEvent.VK_SPACE && !timerManager.isAnyKeyEnabled) {
                    return
                }
                if (keyEvent.keyCode != KeyEvent.VK_SPACE && timerManager.isAnyKeyEnabled && state != State.RUNNING) return
                when (state) {
                    State.RUNNING -> {
                        finish = Date()
                        if (finish.time - start!!.time < 250) {
                            break
                        }
                        repeater!!.cancel()
                        timerManager.finishSolution(
                                Timing(start, finish))
                        state = State.FINISHED
                    }
                }
                timerManager.pressLeftHand()
                timerManager.pressRightHand()
            }

            override fun keyReleased(keyEvent: KeyEvent) {
                if (keyEvent.keyCode != KeyEvent.VK_SPACE && !timerManager.isAnyKeyEnabled) {
                    return
                }
                when (state) {
                    State.READY_FOR_INSPECTION -> {
                        if (keyEvent.keyCode != KeyEvent.VK_SPACE) break
                        if (Date().time - finish.time < 250) {
                            break
                        }
                        timerManager.startInspection()
                        state = State.READY
                    }
                    State.READY -> {
                        if (Date().time - finish.time < 250) {
                            break
                        }
                        timerManager.startSolution()
                        start = Date()
                        repeater = Timer()
                        repeater!!.schedule(object : TimerTask() {
                            override fun run() {
                                timerManager.updateSolutionTiming(
                                        Timing(start, Date()))
                            }
                        }, 0, 5)
                        state = State.RUNNING
                    }
                    State.FINISHED -> state = if (inspectionEnabled) State.READY_FOR_INSPECTION else State.READY
                }
                timerManager.releaseLeftHand()
                timerManager.releaseRightHand()
            }
        }
        frame.addKeyListener(keyListener)
        timerListener = object : TimerManager.Listener() {
            override fun inspectionFinished() {
                state = if (inspectionEnabled) State.READY_FOR_INSPECTION else State.READY
            }
        }
        timerManager.addListener(timerListener)
    }

    override fun stop() {
        if (repeater != null) {
            repeater!!.cancel()
        }
        frame.removeKeyListener(keyListener)
        timerManager.removeListener(timerListener)
    }

    override fun setSmoothTimingEnabled(smoothTimingEnabled: Boolean) {
        // TODO Auto-generated method stub
    }

    init {
        finish = Date(0)
        state = if (inspectionEnabled) State.READY_FOR_INSPECTION else State.READY
    }
}