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
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import java.lang.StringBuilder
import kotlin.Throws
import java.io.IOException
import java.io.BufferedReader
import java.nio.charset.Charset
import java.util.*

class MessageManager {
    open class Listener {
        open fun messagesCleared() {}
        open fun messageReceived(messageType: MessageType?, message: String?) {}
    }

    enum class MessageType {
        INFORMATION, ERROR
    }

    private val messageQueue: ArrayList<String>
    private val messageTypeQueue: ArrayList<MessageType>
    private val listeners: ArrayList<Listener>
    fun enqueueMessage(messageType: MessageType, message: String) {
        synchronized(this) {
            messageTypeQueue.add(messageType)
            messageQueue.add(message)
        }
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    init {
        messageQueue = ArrayList()
        messageTypeQueue = ArrayList()
        listeners = ArrayList()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            private var nextUpdate = Date()
            private var clear = true
            override fun run() {
                val now = Date()
                if (now.time > nextUpdate.time) {
                    if (!clear) {
                        for (listener in listeners) {
                            listener.messagesCleared()
                        }
                        clear = true
                    }
                    synchronized(this@MessageManager) {
                        if (messageTypeQueue.size > 0) {
                            val messageType = messageTypeQueue.removeAt(0)
                            val message = messageQueue.removeAt(0)
                            for (listener in listeners) {
                                listener.messageReceived(messageType, message)
                            }
                            nextUpdate = Date(now.time + 8000)
                            clear = false
                        }
                    }
                }
            }
        }, 0, 250)
    }
}