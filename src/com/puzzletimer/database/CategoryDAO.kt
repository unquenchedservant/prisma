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
import com.puzzletimer.models.*
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import java.lang.RuntimeException
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.parsers.ScrambleParserProvider
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.parsers.ScrambleParser
import java.sql.Connection
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList

class CategoryDAO(  // tips
        private val connection: Connection) {
    // category
    val all: Array<Category?>
        get() {
            val categories = ArrayList<Category>()
            try {
                // category
                val categoryStatement = connection.createStatement()
                val categoryResultSet = categoryStatement.executeQuery(
                        "SELECT CATEGORY_ID, SCRAMBLER_ID, DESCRIPTION, USER_DEFINED FROM CATEGORY " +
                                "ORDER BY \"ORDER\"")
                while (categoryResultSet.next()) {
                    val categoryId = UUID.fromString(categoryResultSet.getString(1))
                    val scramblerId = categoryResultSet.getString(2)
                    val description = categoryResultSet.getString(3)
                    val isUserDefined = categoryResultSet.getBoolean(4)

                    // tips
                    val tipsStatement = connection.prepareStatement(
                            "SELECT TIP_ID FROM CATEGORY_TIPS " +
                                    "WHERE CATEGORY_ID = ? " +
                                    "ORDER BY \"ORDER\"")
                    tipsStatement.setString(1, categoryId.toString())
                    val tipsResultSet = tipsStatement.executeQuery()
                    val tipIds = ArrayList<String>()
                    while (tipsResultSet.next()) {
                        tipIds.add(tipsResultSet.getString(1))
                    }
                    val tipIdsArray = arrayOfNulls<String>(tipIds.size)
                    tipIds.toArray(tipIdsArray)
                    categories.add(
                            Category(
                                    categoryId,
                                    scramblerId,
                                    description,
                                    isUserDefined,
                                    tipIdsArray))
                }
                categoryStatement.close()
            } catch (e: SQLException) {
                throw DatabaseException(e)
            }
            val categoriesArray = arrayOfNulls<Category>(categories.size)
            categories.toArray(categoriesArray)
            return categoriesArray
        }

    fun insert(category: Category) {
        try {
            connection.autoCommit = false

            // category
            val categoryStatement = connection.prepareStatement(
                    "INSERT INTO CATEGORY VALUES (?, ?, ?, ?, ?)")
            categoryStatement.setInt(1, 0)
            categoryStatement.setString(2, category.getCategoryId().toString())
            categoryStatement.setString(3, category.getScramblerId())
            categoryStatement.setString(4, category.getDescription())
            categoryStatement.setBoolean(5, category.isUserDefined)
            categoryStatement.executeUpdate()
            categoryStatement.close()

            // tips
            val tipsStatement = connection.prepareStatement(
                    "INSERT INTO CATEGORY_TIPS VALUES (?, ?, ?)")
            for (i in 0 until category.tipIds.length) {
                tipsStatement.setInt(1, i)
                tipsStatement.setString(2, category.getCategoryId().toString())
                tipsStatement.setString(3, category.tipIds[i])
                tipsStatement.addBatch()
            }
            tipsStatement.executeBatch()
            tipsStatement.close()
            connection.commit()
        } catch (e: SQLException) {
            try {
                connection.rollback()
            } catch (e1: SQLException) {
                throw DatabaseException(e1)
            }
            throw DatabaseException(e)
        } finally {
            try {
                connection.autoCommit = true
            } catch (e: SQLException) {
                throw DatabaseException(e)
            }
        }
    }

    fun update(category: Category) {
        try {
            connection.autoCommit = false

            // category
            val categoryStatement = connection.prepareStatement(
                    "UPDATE CATEGORY SET SCRAMBLER_ID = ?, DESCRIPTION = ?, USER_DEFINED = ? " +
                            "WHERE CATEGORY_ID = ?")
            categoryStatement.setString(1, category.getScramblerId())
            categoryStatement.setString(2, category.getDescription())
            categoryStatement.setBoolean(3, category.isUserDefined)
            categoryStatement.setString(4, category.getCategoryId().toString())
            categoryStatement.executeUpdate()
            categoryStatement.close()

            // delete old tips
            val deleteTipsStatement = connection.prepareStatement(
                    "DELETE FROM CATEGORY_TIPS " +
                            "WHERE CATEGORY_ID = ?")
            deleteTipsStatement.setString(1, category.getCategoryId().toString())
            deleteTipsStatement.executeUpdate()
            deleteTipsStatement.close()

            // tips
            val tipsStatement = connection.prepareStatement(
                    "INSERT INTO CATEGORY_TIPS VALUES (?, ?, ?)")
            for (i in 0 until category.tipIds.length) {
                tipsStatement.setInt(1, i)
                tipsStatement.setString(2, category.getCategoryId().toString())
                tipsStatement.setString(3, category.tipIds[i])
                tipsStatement.addBatch()
            }
            tipsStatement.executeBatch()
            tipsStatement.close()
            connection.commit()
        } catch (e: SQLException) {
            try {
                connection.rollback()
            } catch (e1: SQLException) {
                throw DatabaseException(e1)
            }
            throw DatabaseException(e)
        } finally {
            try {
                connection.autoCommit = true
            } catch (e: SQLException) {
                throw DatabaseException(e)
            }
        }
    }

    fun delete(category: Category) {
        try {
            val statement = connection.prepareStatement(
                    "DELETE FROM CATEGORY " +
                            "WHERE CATEGORY_ID = ?")
            statement.setString(1, category.getCategoryId().toString())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            throw DatabaseException(e)
        }
    }
}