package com.puzzletimer.state

import com.puzzletimer.models.*
import com.puzzletimer.timer.Timer.stop
import com.puzzletimer.timer.Timer.setInspectionEnabled
import com.puzzletimer.timer.Timer.start
import com.puzzletimer.timer.Timer.setSmoothTimingEnabled
import java.util.Arrays
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import java.util.TimerTask
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.scramblers.Scrambler
import java.util.Collections
import java.util.UUID
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

class ScrambleManager(scramblerProvider: ScramblerProvider, scrambler: Scrambler) {
    open class Listener {
        fun scramblesAdded(scrambles: Array<Scramble?>?) {}
        fun scramblesRemoved(scrambles: Array<Scramble?>?) {}
        open fun scrambleQueueUpdated(queue: Array<Scramble?>?) {}
        open fun scrambleChanged(scramble: Scramble?) {}
    }

    private val listeners: ArrayList<Listener>
    private val scramblerProvider: ScramblerProvider
    private var currentScrambler: Scrambler
    private val queue: ArrayList<Scramble?>
    var currentScramble: Scramble?
        private set

    fun setCategory(category: Category) {
        queue.clear()
        currentScrambler = scramblerProvider[category.scramblerId]
        changeScramble()
        notifyListeners()
    }

    fun getQueue(): Array<Scramble?> {
        val queueArray = arrayOfNulls<Scramble>(queue.size)
        queue.toArray(queueArray)
        return queueArray
    }

    fun addScrambles(scrambles: Array<Scramble?>, priority: Boolean) {
        if (priority) {
            for (scramble in scrambles) {
                queue.add(0, scramble)
            }
        } else {
            Collections.addAll(queue, *scrambles)
        }
        for (listener in listeners) {
            listener.scramblesAdded(scrambles)
        }
        notifyListeners()
    }

    fun removeScrambles(indices: IntArray) {
        val scrambles = arrayOfNulls<Scramble>(indices.size)
        for (i in scrambles.indices) {
            scrambles[i] = queue[indices[i]]
        }
        for (scramble in scrambles) {
            queue.remove(scramble)
        }
        for (listener in listeners) {
            listener.scramblesRemoved(scrambles)
        }
        notifyListeners()
    }

    fun moveScramblesUp(indices: IntArray) {
        for (indice in indices) {
            val temp = queue[indice - 1]
            queue[indice - 1] = queue[indice]
            queue[indice] = temp
        }
        notifyListeners()
    }

    fun moveScramblesDown(indices: IntArray) {
        for (i in indices.indices.reversed()) {
            val temp = queue[indices[i] + 1]
            queue[indices[i] + 1] = queue[indices[i]]
            queue[indices[i]] = temp
        }
        notifyListeners()
    }

    fun changeScramble() {
        if (queue.size > 0) {
            currentScramble = queue[0]
            removeScrambles(intArrayOf(0))
        } else {
            currentScramble = currentScrambler.nextScramble
        }
        for (listener in listeners) {
            listener.scrambleChanged(currentScramble)
        }
    }

    private fun notifyListeners() {
        val queueArray = arrayOfNulls<Scramble>(queue.size)
        queue.toArray(queueArray)
        for (listener in listeners) {
            listener.scrambleQueueUpdated(queueArray)
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
        this.scramblerProvider = scramblerProvider
        currentScrambler = scrambler
        queue = ArrayList()
        currentScramble = currentScrambler.nextScramble
    }
}