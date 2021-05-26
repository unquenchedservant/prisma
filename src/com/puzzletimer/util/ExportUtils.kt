package com.puzzletimer.util

import com.puzzletimer.util.StringUtils.join
import com.puzzletimer.util.SolutionUtils.formatSeconds
import com.puzzletimer.models.Solution
import com.puzzletimer.state.SolutionManager
import com.puzzletimer.util.ExportUtils
import java.text.SimpleDateFormat
import java.io.PrintWriter
import com.puzzletimer.util.SolutionUtils
import javax.swing.JOptionPane
import com.puzzletimer.statistics.Average
import java.lang.Exception
import java.util.*

object ExportUtils {
    private var currentFive: ArrayList<Solution>? = null
    private var currentTwelve: ArrayList<Solution>? = null
    private var currentHundred: ArrayList<Solution>? = null
    fun ExportToFile(solutionManager: SolutionManager) {
        // clear these out each export so we're not carrying data from previous export
        currentFive = ArrayList()
        currentTwelve = ArrayList()
        currentHundred = ArrayList()
        val fileDate = SimpleDateFormat("dd-MM-yyyy hhmm a").format(Date())
        val fileName = fileDate + " " + keyify("tools.export_filename")

        // we want the solutions to be in reverse order (earliest to latest)
        // so calculated averages are correct (i.e. Ao12 is of the current solve
        // and the 11 previous solves)
        val solutions = ArrayList<Solution>()
        for (solution in solutionManager.solutions) {
            solutions.add(0, solution)
        }
        try {
            val columnHeaders = arrayOf<String?>("Solution Date & Time", "Scramble", "Time Elapsed (seconds)", "Current Ao5",
                    "Current Ao12", "Current Ao100")
            val writer = PrintWriter(fileName)
            writer.write("""
    ${join(",", columnHeaders)}
    
    """.trimIndent())
            for (solution in solutions) {
                val date = solution.timing.start.toString()
                val timeElapsed = formatSeconds(solution.timing.elapsedTime, "MILLISECONDS", false)
                val scramble = solution.scramble.rawSequence
                val ao5 = calculateAverageOfFive(solution)
                val ao12 = calculateAverageOfTwelve(solution)
                val ao100 = calculateAverageOfHundred(solution)
                val rowValues = arrayOf(date, scramble, timeElapsed, ao5, ao12, ao100)
                writer.write("""
    ${join(",", rowValues)}
    
    """.trimIndent())
            }
            writer.close()
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Unable to export solutions: " + e.message, "Error", JOptionPane.ERROR_MESSAGE)
            return
        }
        val message = solutions.size.toString() + " solutions successfully exported to " + fileName
        JOptionPane.showMessageDialog(null, message, "Complete!", JOptionPane.PLAIN_MESSAGE)
    }

    private fun calculateAverageOfFive(solution: Solution): String {
        currentFive!!.add(solution)
        if (currentFive!!.size > 5) {
            currentFive!!.removeAt(0)
        }
        return if (currentFive!!.size == 5) {
            val aoFive = Average(0, 1)
            aoFive.setSolutions(currentFive!!.toTypedArray(), false)
            "" + formatSeconds(aoFive.value, "MILLISECONDS", false)
        } else {
            ""
        }
    }

    private fun calculateAverageOfTwelve(solution: Solution): String {
        currentTwelve!!.add(solution)
        if (currentTwelve!!.size > 12) {
            currentTwelve!!.removeAt(0)
        }
        return if (currentTwelve!!.size == 12) {
            val aoTwelve = Average(0, 1)
            aoTwelve.setSolutions(currentTwelve!!.toTypedArray(), false)
            "" + formatSeconds(aoTwelve.value, "MILLISECONDS", false)
        } else {
            ""
        }
    }

    private fun calculateAverageOfHundred(solution: Solution): String {
        currentHundred!!.add(solution)
        if (currentHundred!!.size > 100) {
            currentHundred!!.removeAt(0)
        }
        return if (currentHundred!!.size == 100) {
            val aoHundred = Average(0, 1)
            aoHundred.setSolutions(currentHundred!!.toTypedArray(), false)
            "" + formatSeconds(aoHundred.value, "MILLISECONDS", false)
        } else {
            ""
        }
    }
}