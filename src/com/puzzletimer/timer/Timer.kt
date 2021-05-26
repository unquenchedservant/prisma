package com.puzzletimer.timer

interface Timer {
    val timerId: String?
    fun setInspectionEnabled(inspectionEnabled: Boolean)
    fun setSmoothTimingEnabled(smoothTimingEnabled: Boolean)
    fun start()
    fun stop()
}