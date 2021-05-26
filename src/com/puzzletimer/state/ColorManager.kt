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

class ColorManager(colorSchemes: Array<ColorScheme>) {
    open class Listener {
        open fun colorSchemeUpdated(colorScheme: ColorScheme?) {}
    }

    private val listeners: ArrayList<Listener>
    private val colorSchemeMap: HashMap<String, ColorScheme>
    fun getColorScheme(puzzleId: String): ColorScheme? {
        return if (colorSchemeMap.containsKey(puzzleId)) {
            colorSchemeMap[puzzleId]
        } else ColorScheme(puzzleId, arrayOfNulls(0))
    }

    fun setColorScheme(colorScheme: ColorScheme) {
        colorSchemeMap[colorScheme.puzzleId] = colorScheme
        for (listener in listeners) {
            listener.colorSchemeUpdated(colorScheme)
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
        colorSchemeMap = HashMap()
        for (colorScheme in colorSchemes) {
            colorSchemeMap[colorScheme.puzzleId] = colorScheme
        }
    }
}