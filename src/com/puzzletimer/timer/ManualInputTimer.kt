package com.puzzletimer.timer

import com.puzzletimer.util.SolutionUtils.parseTime
import com.puzzletimer.state.TimerManager
import javax.swing.JTextField
import java.awt.event.KeyListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.models.Timing
import java.util.*

class ManualInputTimer(private val timerManager: TimerManager, private val textFieldTime: JTextField) : Timer {
    private var keyListener: KeyListener? = null
    private var start: Date? = null
    override fun getTimerId(): String {
        return "MANUAL-INPUT"
    }

    override fun start() {
        keyListener = object : KeyAdapter() {
            override fun keyPressed(keyEvent: KeyEvent) {
                if (keyEvent.keyCode != KeyEvent.VK_ENTER) {
                    return
                }
                start = Date()
                val time = parseTime(
                        textFieldTime.text)
                val timing = Timing(
                        start,
                        Date(start!!.time + time))
                timerManager.finishSolution(timing)
                textFieldTime.text = null
            }
        }
        textFieldTime.addKeyListener(keyListener)
    }

    override fun setInspectionEnabled(inspectionEnabled: Boolean) {}
    override fun stop() {
        textFieldTime.removeKeyListener(keyListener)
    }

    override fun setSmoothTimingEnabled(smoothTimingEnabled: Boolean) {
        // TODO Auto-generated method stub
    }
}