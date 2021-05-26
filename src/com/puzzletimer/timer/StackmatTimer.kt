// reference: http://hackvalue.de/hv_atmel_stackmat
package com.puzzletimer.timer

import javax.sound.sampled.TargetDataLine
import com.puzzletimer.state.TimerManager
import java.lang.Runnable
import com.puzzletimer.timer.StackmatTimerReaderListener
import com.puzzletimer.gui.StackmatDeveloperFrame
import com.puzzletimer.timer.StackmatTimerReader
import com.puzzletimer.models.Timing
import java.util.*

internal interface StackmatTimerReaderListener {
    fun dataReceived(data: ByteArray, hasSixDigits: Boolean)
}

internal class StackmatTimerReader(targetDataLine: TargetDataLine, timerManager: TimerManager) : Runnable {
    private val sampleRate: Double
    private var baudRateOffset: Int
    private var previousBaudRate: Int
    private var baudRate: Int
    private var period: Double
    private val targetDataLine: TargetDataLine
    private val listeners: ArrayList<StackmatTimerReaderListener>
    private var running: Boolean
    private var hasSixDigits: Boolean
    private val timerManager: TimerManager
    private fun readPacket(samples: ByteArray, offset: Int, bitThreshold: Byte, isInverted: Boolean): ByteArray {
        val data = ByteArray(10)
        for (i in 0..8) {
            // start bit
            val startBit = samples[offset + (10 * i * period).toInt()] <= bitThreshold
            if (isInverted && startBit || !isInverted && !startBit) {
                return ByteArray(10) // invalid data
            }

            // data bits
            data[i] = 0x00
            for (j in 0..7) {
                if (samples[offset + ((10 * i + j + 1) * period).toInt()] > bitThreshold) {
                    data[i] = data[i] or (0x01 shl j)
                }
            }
            if (isInverted) {
                data[i] = data[i].inv() as Byte
            }

            // stop bit
            val stopBit = samples[offset + ((10 * i + 9) * period).toInt()] <= bitThreshold
            if (isInverted && !stopBit || !isInverted && stopBit) {
                return ByteArray(10) // invalid data
            }
        }
        if (data[8] == '\n') hasSixDigits = true
        if (data[8] == '\r') hasSixDigits = false
        if (hasSixDigits) {
            val i = 9
            // start bit
            val startBit = samples[offset + (10 * i * period).toInt()] <= bitThreshold
            if (isInverted && startBit || !isInverted && !startBit) {
                return ByteArray(10) // invalid data
            }

            // data bits
            data[i] = 0x00
            for (j in 0..7) {
                if (samples[offset + ((10 * i + j + 1) * period).toInt()] > bitThreshold) {
                    data[i] = data[i] or (0x01 shl j)
                }
            }
            if (isInverted) {
                data[i] = data[i].inv() as Byte
            }

            // stop bit
            val stopBit = samples[offset + ((10 * i + 9) * period).toInt()] <= bitThreshold
            if (isInverted && !stopBit || !isInverted && stopBit) {
                return ByteArray(10) // invalid data
            }
        }
        return data
    }

    private fun isValidPacket(data: ByteArray): Boolean {
        var sum = 0
        for (i in 1 until if (hasSixDigits) 7 else 6) {
            sum += data[i] - '0'
        }
        return if (hasSixDigits) " ACILRS".contains(data[0] as Char.toString()) &&
                Character.isDigit(data[1]) &&
                Character.isDigit(data[2]) &&
                Character.isDigit(data[3]) &&
                Character.isDigit(data[4]) &&
                Character.isDigit(data[5]) &&
                Character.isDigit(data[6]) && data[7] == sum + 64 && data[8] == '\n' && data[9] == '\r' else " ACILRS".contains(data[0] as Char.toString()) &&
                Character.isDigit(data[1]) &&
                Character.isDigit(data[2]) &&
                Character.isDigit(data[3]) &&
                Character.isDigit(data[4]) &&
                Character.isDigit(data[5]) && data[6] == sum + 64 && data[7] == '\n' && data[8] == '\r'
    }

    override fun run() {
        running = true
        targetDataLine.start()
        val buffer = ByteArray((sampleRate / 4).toInt())
        var offset = buffer.size
        while (running) {
            // update buffer in a queue fashion
            System.arraycopy(buffer, offset, buffer, offset - offset, buffer.size - offset)
            targetDataLine.read(buffer, buffer.size - offset, offset)
            //System.out.println(buffer.length);
            //System.out.println(offset);
            //System.out.println();
            var isPacketStart = false
            var isSignalInverted = false
            baudRateOffset = 0
            loop@ while (baudRateOffset < 25) {
                baudRate = previousBaudRate + baudRateOffset
                period = sampleRate / baudRate.toDouble()
                offset = 0
                while (offset + (if (hasSixDigits) 0.132015 else 0.119181) * sampleRate < buffer.size) {
                    for (threshold in 0..255) {
                        var data = readPacket(buffer, offset, (threshold - 127).toByte(), false)
                        if (isValidPacket(data)) {
                            isPacketStart = true
                            break@loop
                        }

                        // try inverting the signal
                        data = readPacket(buffer, offset, (threshold - 127).toByte(), true)
                        if (isValidPacket(data)) {
                            isPacketStart = true
                            isSignalInverted = true
                            break@loop
                        }
                    }
                    offset++
                }
                baudRateOffset = -baudRateOffset
                if (baudRateOffset < 0) baudRateOffset--
                baudRateOffset++
            }
            if (!isPacketStart) {
                timerManager.dataNotReceived(buffer)
                println(false)
                continue
            }
            val baudRateHistogram = HashMap<Int, Int>()
            run {
                var i = 0
                while (i < 10) {
                    var j = 0
                    while (j < this.period) {
                        this.baudRate = this.previousBaudRate + this.baudRateOffset + i
                        this.period = this.sampleRate / this.baudRate.toDouble()
                        val data = readPacket(buffer, offset + j, 0.toByte(), isSignalInverted)
                        if (isValidPacket(data)) {
                            if (baudRateHistogram.containsKey(this.previousBaudRate + this.baudRateOffset + i)) {
                                baudRateHistogram[this.previousBaudRate + this.baudRateOffset + i] = baudRateHistogram[this.previousBaudRate + this.baudRateOffset + i]!! + 1
                            } else {
                                baudRateHistogram[this.previousBaudRate + this.baudRateOffset + i] = 1
                            }
                        }
                        j++
                    }
                    i = -i
                    if (i < 0) i--
                    i++
                }
            }
            var highestFrequencyBaudRate = 0
            for ((key, value) in baudRateHistogram) {
                if (value > highestFrequencyBaudRate) {
                    baudRate = key
                    highestFrequencyBaudRate = value
                }
            }
            period = sampleRate / baudRate
            previousBaudRate = baudRate

            // create packet histogram
            val packetHistogram = HashMap<Long, Int>()
            run {
                var i = 0
                while (i < this.period) {
                    for (threshold in 0..255) {
                        val data = readPacket(buffer, offset + i, (threshold - 127).toByte(), isSignalInverted)
                        if (isValidPacket(data)) {
                            // encode packet
                            var packet = 0L
                            for (j in 0 until if (hasSixDigits) 7 else 6) {
                                packet = packet or (data[j].toLong() shl 8 * j)
                            }
                            if (packetHistogram.containsKey(packet)) {
                                packetHistogram[packet] = packetHistogram[packet]!! + 1
                            } else {
                                packetHistogram[packet] = 1
                            }
                        }
                    }
                    i++
                }
            }

            // select packet with highest frequency
            var packet = 0L
            var highestFrequency = 0
            for ((key, value) in packetHistogram) {
                if (value > highestFrequency) {
                    packet = key
                    highestFrequency = value
                }
            }

            // decode packet
            val data = ByteArray(9)
            for (i in 0 until if (hasSixDigits) 7 else 6) {
                data[i] = (packet shr 8 * i).toByte()
            }
            timerManager.dataNotReceived(buffer)

            // notify listeners
            for (listener in listeners) {
                listener.dataReceived(data, hasSixDigits)
            }

            // skip read packet
            offset += ((if (hasSixDigits) 0.132015 else 0.119181) * sampleRate).toInt()
        }
        targetDataLine.close()
    }

    fun stop() {
        running = false
    }

    fun addEventListener(listener: StackmatTimerReaderListener) {
        listeners.add(listener)
    }

    fun removeEventListener(listener: StackmatTimerReaderListener) {
        listeners.remove(listener)
    }

    init {
        sampleRate = targetDataLine.format.frameRate.toDouble()
        baudRateOffset = 0
        previousBaudRate = 1200
        baudRate = previousBaudRate + baudRateOffset
        period = sampleRate / baudRate.toDouble()
        this.targetDataLine = targetDataLine
        listeners = ArrayList()
        running = false
        hasSixDigits = false
        this.timerManager = timerManager
    }
}

class StackmatTimer(targetDataLine: TargetDataLine, timerManager: TimerManager, stackmatDeveloperFrame: StackmatDeveloperFrame?) : StackmatTimerReaderListener, Timer {
    private enum class State {
        NOT_READY, RESET_FOR_INSPECTION, RESET, READY, RUNNING
    }

    private val stackmatTimerReader: StackmatTimerReader
    private val timerManager: TimerManager
    private var inspectionEnabled: Boolean
    private var smoothTimingEnabled: Boolean
    private var timerListener: TimerManager.Listener? = null
    private var repeater: java.util.Timer? = null
    private var start: Date?
    private var state: State
    private var previousTime: Long
    override fun getTimerId(): String {
        return "STACKMAT-TIMER"
    }

    override fun setInspectionEnabled(inspectionEnabled: Boolean) {
        this.inspectionEnabled = inspectionEnabled
        when (state) {
            State.RESET_FOR_INSPECTION -> if (!inspectionEnabled) {
                state = State.RESET
            }
            State.RESET -> if (inspectionEnabled) {
                state = State.RESET_FOR_INSPECTION
            }
        }
    }

    override fun start() {
        timerListener = object : TimerManager.Listener() {
            override fun inspectionFinished() {
                state = State.NOT_READY
            }
        }
        timerManager.addListener(timerListener)
        stackmatTimerReader.addEventListener(this)
        val readerThread = Thread(stackmatTimerReader)
        readerThread.start()
        repeater = Timer()
        repeater!!.schedule(object : TimerTask() {
            override fun run() {
                when (this@StackmatTimer.state) {
                    State.RUNNING -> if (smoothTimingEnabled) timerManager.updateSolutionTiming(
                            Timing(start, Date()))
                }
            }
        }, 0, 5)
    }

    override fun stop() {
        timerManager.removeListener(timerListener)
        stackmatTimerReader.removeEventListener(this)
        stackmatTimerReader.stop()
        repeater!!.cancel()
    }

    override fun dataReceived(data: ByteArray, hasSixDigits: Boolean) {
        // hands status
        if (data[0] == 'A' || data[0] == 'L' || data[0] == 'C') {
            timerManager.pressLeftHand()
        } else {
            timerManager.releaseLeftHand()
        }
        if (data[0] == 'A' || data[0] == 'R' || data[0] == 'C') {
            timerManager.pressRightHand()
        } else {
            timerManager.releaseRightHand()
        }

        // time
        val minutes: Int = data[1] - '0'
        val seconds: Int = 10 * (data[2] - '0') + data[3] - '0'.toInt()
        val time: Long
        time = if (hasSixDigits) {
            val milliseconds: Int = 100 * (data[4] - '0') + 10 * (data[5] - '0') + data[6] - '0'.toInt()
            (60000 * minutes + 1000 * seconds + milliseconds).toLong()
        } else {
            val centiseconds: Int = 10 * (data[4] - '0') + data[5] - '0'.toInt()
            (60000 * minutes + 1000 * seconds + 10 * centiseconds).toLong()
        }
        val end = Date()
        val start = Date(end.time - time)
        val timing = Timing(start, end)
        this.start = start
        when (state) {
            State.NOT_READY -> {
                timerManager.updateSolutionTiming(timing)

                // timer initialized
                if (time == 0L) {
                    timerManager.resetTimer()
                    state = if (inspectionEnabled) State.RESET_FOR_INSPECTION else State.RESET
                }
            }
            State.RESET_FOR_INSPECTION ->                 // some pad pressed
                if (data[0] == 'L' || data[0] == 'R' || data[0] == 'C') {
                    timerManager.startInspection()
                    state = State.RESET
                }
            State.RESET -> {
                if (!inspectionEnabled) timerManager.updateSolutionTiming(timing)

                // ready to start
                if (data[0] == 'A') {
                    state = State.READY
                }

                // timing started
                if (time > 0) {
                    timerManager.startSolution()
                    state = State.RUNNING
                }
            }
            State.READY -> {
                timerManager.updateSolutionTiming(timing)

                // timing started
                if (time > 0) {
                    timerManager.startSolution()
                    state = State.RUNNING
                }
            }
            State.RUNNING -> {
                timerManager.updateSolutionTiming(timing)

                // timer reset during solution
                if (time == 0L) {
                    state = State.NOT_READY
                }

                // timer stopped
                if (data[0] == 'S' || time == previousTime) {
                    state = State.NOT_READY
                    timerManager.finishSolution(timing)
                }
            }
        }
        previousTime = time
    }

    override fun setSmoothTimingEnabled(smoothTimingEnabled: Boolean) {
        this.smoothTimingEnabled = smoothTimingEnabled
    }

    init {
        stackmatTimerReader = StackmatTimerReader(targetDataLine, timerManager)
        this.timerManager = timerManager
        inspectionEnabled = false
        smoothTimingEnabled = true
        start = null
        state = State.NOT_READY
        previousTime = -1
    }
}