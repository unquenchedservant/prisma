package com.puzzletimer.timer

import javax.swing.JFrame
import com.puzzletimer.state.TimerManager
import java.awt.event.KeyListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import com.puzzletimer.models.Timing
import java.util.*

class ControlKeysTimer(private val frame: JFrame, private val timerManager: TimerManager) : Timer {
    private enum class State {
        READY_FOR_INSPECTION, NOT_READY, READY, RUNNING, FINISHED
    }

    private var inspectionEnabled = false
    private var leftPressed = false
    private var rightPressed = false
    private var keyListener: KeyListener? = null
    private var timerListener: TimerManager.Listener? = null
    private var repeater: java.util.Timer? = null
    private var start: Date? = null
    private var finish: Date
    private var state: State
    override fun getTimerId(): String {
        return "KEYBOARD-TIMER-CONTROL"
    }

    override fun setInspectionEnabled(inspectionEnabled: Boolean) {
        this.inspectionEnabled = inspectionEnabled
        when (state) {
            State.READY_FOR_INSPECTION -> if (!inspectionEnabled) {
                state = State.NOT_READY
            }
            State.NOT_READY -> if (inspectionEnabled) {
                state = State.READY_FOR_INSPECTION
            }
        }
    }

    override fun start() {
        keyListener = object : KeyAdapter() {
            override fun keyPressed(keyEvent: KeyEvent) {
                if (keyEvent.keyCode != KeyEvent.VK_CONTROL) {
                    return
                }
                when (keyEvent.keyLocation) {
                    KeyEvent.KEY_LOCATION_LEFT -> {
                        leftPressed = true
                        timerManager.pressLeftHand()
                    }
                    KeyEvent.KEY_LOCATION_RIGHT -> {
                        rightPressed = true
                        timerManager.pressRightHand()
                    }
                }
                when (state) {
                    State.READY_FOR_INSPECTION -> {
                        if (Date().time - finish.time < 250) {
                            break
                        }
                        timerManager.startInspection()
                        state = State.NOT_READY
                    }
                    State.NOT_READY -> if (leftPressed && rightPressed) {
                        state = State.READY
                    }
                    State.RUNNING -> if (leftPressed && rightPressed) {
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
            }

            override fun keyReleased(keyEvent: KeyEvent) {
                if (keyEvent.keyCode != KeyEvent.VK_CONTROL) {
                    return
                }
                when (keyEvent.keyLocation) {
                    KeyEvent.KEY_LOCATION_LEFT -> {
                        leftPressed = false
                        timerManager.releaseLeftHand()
                    }
                    KeyEvent.KEY_LOCATION_RIGHT -> {
                        rightPressed = false
                        timerManager.releaseRightHand()
                    }
                }
                when (state) {
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
                    State.FINISHED -> if (!leftPressed && !rightPressed) {
                        state = if (inspectionEnabled) State.READY_FOR_INSPECTION else State.NOT_READY
                    }
                }
            }
        }
        frame.addKeyListener(keyListener)
        timerListener = object : TimerManager.Listener() {
            override fun inspectionFinished() {
                state = if (inspectionEnabled) State.READY_FOR_INSPECTION else State.NOT_READY
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
        state = if (inspectionEnabled) State.READY_FOR_INSPECTION else State.NOT_READY
    }
}