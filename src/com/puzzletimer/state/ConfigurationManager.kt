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

class ConfigurationManager(entries: Array<ConfigurationEntry>) {
    open class Listener {
        open fun configurationEntryUpdated(key: String?, value: String?) {}
    }

    private val listeners: ArrayList<Listener>
    private val entryMap: HashMap<String, ConfigurationEntry>
    fun getConfiguration(key: String): String? {
        val configurationEntry = entryMap[key] ?: return null
        return configurationEntry.value
    }

    fun setConfiguration(key: String, value: String?) {
        entryMap[key] = ConfigurationEntry(key, value)
        for (listener in listeners) {
            listener.configurationEntryUpdated(key, value)
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
        entryMap = HashMap()
        for (entry in entries) {
            entryMap[entry.key] = entry
        }
    }
}