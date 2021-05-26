package com.puzzletimer.database

import com.puzzletimer.models.Category.getCategoryId
import com.puzzletimer.models.Category.getScramblerId
import com.puzzletimer.models.Category.getDescription
import com.puzzletimer.models.Category.isUserDefined
import com.puzzletimer.models.Category.tipIds
import com.puzzletimer.models.ColorScheme.faceColors
import com.puzzletimer.models.ColorScheme.FaceColor.color
import com.puzzletimer.models.ColorScheme.puzzleId
import com.puzzletimer.models.ColorScheme.FaceColor.faceId
import com.puzzletimer.models.ConfigurationEntry.value
import com.puzzletimer.models.ConfigurationEntry.key
import com.puzzletimer.scramblers.ScramblerProvider.get
import com.puzzletimer.parsers.ScrambleParserProvider.get
import com.puzzletimer.scramblers.Scrambler.scramblerInfo
import com.puzzletimer.models.ScramblerInfo.puzzleId
import com.puzzletimer.parsers.ScrambleParser.parse
import com.puzzletimer.models.Solution.solutionId
import com.puzzletimer.models.Solution.categoryId
import com.puzzletimer.models.Solution.scramble
import com.puzzletimer.models.Scramble.scramblerId
import com.puzzletimer.models.Scramble.rawSequence
import com.puzzletimer.models.Solution.timing
import com.puzzletimer.models.Timing.start
import com.puzzletimer.models.Timing.end
import com.puzzletimer.models.Solution.penalty
import com.puzzletimer.models.Solution.comment
import java.sql.ResultSet
import java.util.UUID
import java.sql.PreparedStatement
import java.sql.SQLException
import com.puzzletimer.database.DatabaseException
import com.puzzletimer.models.ColorScheme
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import com.puzzletimer.models.ConfigurationEntry
import java.lang.RuntimeException
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.parsers.ScrambleParserProvider
import com.puzzletimer.models.Solution
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.parsers.ScrambleParser
import com.puzzletimer.models.Scramble
import com.puzzletimer.models.Timing
import java.sql.Connection
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList

class ColorDAO(private val connection: Connection) {
    val all: Array<ColorScheme?>
        get() {
            val faceColorMap = HashMap<String, ArrayList<FaceColor>>()
            try {
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(
                        "SELECT PUZZLE_ID, FACE_ID, DEFAULT_R, DEFAULT_G, DEFAULT_B, R, G, B FROM COLOR " +
                                "ORDER BY \"ORDER\"")
                while (resultSet.next()) {
                    val puzzleId = resultSet.getString(1)
                    val faceId = resultSet.getString(2)
                    val defaultR = resultSet.getInt(3)
                    val defaultG = resultSet.getInt(4)
                    val defaultB = resultSet.getInt(5)
                    val r = resultSet.getInt(6)
                    val g = resultSet.getInt(7)
                    val b = resultSet.getInt(8)
                    if (!faceColorMap.containsKey(puzzleId)) {
                        faceColorMap[puzzleId] = ArrayList()
                    }
                    faceColorMap[puzzleId]!!.add(
                            FaceColor(
                                    puzzleId,
                                    faceId,
                                    Color(defaultR, defaultG, defaultB),
                                    Color(r, g, b)))
                }
            } catch (e: SQLException) {
                throw DatabaseException(e)
            }
            val colorSchemes = ArrayList<ColorScheme>()
            for (puzzleId in faceColorMap.keys) {
                val faceColors = arrayOfNulls<FaceColor>(faceColorMap[puzzleId]!!.size)
                faceColorMap[puzzleId]!!.toArray(faceColors)
                colorSchemes.add(ColorScheme(puzzleId, faceColors))
            }
            val colorSchemesArray = arrayOfNulls<ColorScheme>(colorSchemes.size)
            colorSchemes.toArray(colorSchemesArray)
            return colorSchemesArray
        }

    fun update(colorScheme: ColorScheme) {
        try {
            connection.autoCommit = false
            val statement = connection.prepareStatement(
                    "UPDATE COLOR SET R = ?, G = ?, B = ? WHERE PUZZLE_ID = ? AND FACE_ID = ?")
            for (faceColor in colorScheme.faceColors) {
                statement.setInt(1, faceColor!!.color.red)
                statement.setInt(2, faceColor.color.green)
                statement.setInt(3, faceColor.color.blue)
                statement.setString(4, colorScheme.puzzleId)
                statement.setString(5, faceColor.faceId)
                statement.addBatch()
            }
            statement.executeBatch()
            statement.close()
            connection.commit()
        } catch (e: SQLException) {
            throw DatabaseException(e)
        } finally {
            try {
                connection.autoCommit = true
            } catch (e: SQLException) {
                throw DatabaseException(e)
            }
        }
    }
}