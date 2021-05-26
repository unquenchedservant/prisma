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
import java.io.InputStreamReader
import java.io.Reader
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

object UpdateManager {
    fun checkUpdate(): Boolean {
        return normalisedVersion(getVersionNumber(latest), ".", 3).compareTo(normalisedVersion(_("about.version"), ".", 3)) > 0
    }

    val latest: JSONObject
        get() {
            try {
                val releases = readJsonFromUrl("https://api.github.com/repos/Moony22/prisma/releases")
                var latestNormalised: String? = ""
                var latestRelease = releases.getJSONObject(0)
                for (i in 0 until releases.length()) {
                    val release = releases.getJSONObject(i)
                    val releaseTag = normalisedVersion(release.getString("tag_name"), ".", 3)
                    if (releaseTag.compareTo(latestNormalised!!) > 0) {
                        latestNormalised = releaseTag
                        latestRelease = release
                    }
                }
                return latestRelease
            } catch (e: Exception) {
            }
            return JSONObject()
        }

    fun getVersionNumber(release: JSONObject): String {
        try {
            return release.getString("tag_name")
        } catch (e: JSONException) {
        }
        return ""
    }

    fun getDescription(release: JSONObject): String {
        try {
            return release.getString("body")
        } catch (e: JSONException) {
        }
        return ""
    }

    fun normalisedVersion(version: String?, sep: String?, maxWidth: Int): String {
        val split = Pattern.compile(sep, Pattern.LITERAL).split(version)
        val sb = StringBuilder()
        for (s in split) {
            sb.append(String.format("%" + maxWidth + 's', s))
        }
        return sb.toString()
    }

    @Throws(IOException::class)
    private fun readAll(rd: Reader): String {
        val sb = StringBuilder()
        var cp: Int
        while (rd.read().also { cp = it } != -1) {
            sb.append(cp.toChar())
        }
        return sb.toString()
    }

    @Throws(IOException::class, JSONException::class)
    fun readJsonFromUrl(url: String?): JSONArray {
        val `is` = URL(url).openStream()
        return try {
            val rd = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))
            val jsonText = readAll(rd)
            JSONArray(jsonText)
        } finally {
            `is`.close()
        }
    }
}