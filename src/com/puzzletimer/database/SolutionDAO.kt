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
import com.puzzletimer.database.DatabaseException
import com.puzzletimer.models.*
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import java.lang.RuntimeException
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.parsers.ScrambleParserProvider
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.parsers.ScrambleParser
import java.sql.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class SolutionDAO(
        private val connection: Connection,
        private val scramblerProvider: ScramblerProvider,
        private val scrambleParserProvider: ScrambleParserProvider) {
    fun getAll(category: Category): Array<Solution?> {
        val scrambler = scramblerProvider[category.getScramblerId()!!]
        val scramblerParser = scrambleParserProvider[scrambler!!.scramblerInfo.puzzleId]
        val solutions = ArrayList<Solution>()
        try {
            val statement = connection.prepareStatement(
                    "SELECT SOLUTION_ID, CATEGORY_ID, SCRAMBLER_ID, SEQUENCE, START, END, PENALTY, COMMENT " +
                            "FROM SOLUTION " +
                            "WHERE CATEGORY_ID = ? " +
                            "ORDER BY START DESC")
            statement.setString(1, category.getCategoryId().toString())
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val solutionId = UUID.fromString(resultSet.getString(1))
                val categoryId = UUID.fromString(resultSet.getString(2))
                val scramblerId = resultSet.getString(3)
                val sequence = resultSet.getString(4)
                val start: Date = resultSet.getTimestamp(5)
                val end: Date = resultSet.getTimestamp(6)
                val penalty = resultSet.getString(7)
                val comment = resultSet.getString(8)
                val scramble = Scramble(scramblerId, scramblerParser!!.parse(sequence))
                val solution = Solution(solutionId, categoryId, scramble, Timing(start, end), penalty, comment)
                solutions.add(solution)
            }
        } catch (e: SQLException) {
            throw DatabaseException(e)
        }
        val solutionArray = arrayOfNulls<Solution>(solutions.size)
        solutions.toArray(solutionArray)
        return solutionArray
    }

    fun insert(solution: Solution) {
        insert(arrayOf(solution))
    }

    fun insert(solutions: Array<Solution>) {
        try {
            connection.autoCommit = false
            val statement = connection.prepareStatement(
                    "INSERT INTO SOLUTION VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
            for (solution in solutions) {
                statement.setString(1, solution.solutionId.toString())
                statement.setString(2, solution.categoryId.toString())
                statement.setString(3, solution.scramble.scramblerId)
                statement.setString(4, solution.scramble.rawSequence)
                statement.setTimestamp(5, Timestamp(solution.timing.start.time))
                statement.setTimestamp(6, Timestamp(solution.timing.end!!.time))
                statement.setString(7, solution.penalty)
                statement.setString(8, solution.comment)
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

    fun getCurrentSession(category: Category): Array<Solution?> {
        val scrambler = scramblerProvider[category.getScramblerId()!!]
        val scramblerParser = scrambleParserProvider[scrambler!!.scramblerInfo.puzzleId]
        val solutions = ArrayList<Solution>()
        val dateFormat: DateFormat = SimpleDateFormat("YYYY-MM-dd")
        try {
            val statement = connection.prepareStatement(
                    "SELECT SOLUTION_ID, CATEGORY_ID, SCRAMBLER_ID, SEQUENCE, START, END, PENALTY " +
                            "FROM SOLUTION " +
                            "WHERE START >= ? " +
                            "AND CATEGORY_ID = ?" +
                            "ORDER BY START DESC")
            statement.setString(1, dateFormat.format(Date()))
            statement.setString(2, category.getCategoryId().toString())
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val solutionId = UUID.fromString(resultSet.getString(1))
                val categoryId = UUID.fromString(resultSet.getString(2))
                val scramblerId = resultSet.getString(3)
                val sequence = resultSet.getString(4)
                val start: Date = resultSet.getTimestamp(5)
                val end: Date = resultSet.getTimestamp(6)
                val penalty = resultSet.getString(7)
                val scramble = Scramble(scramblerId, scramblerParser!!.parse(sequence))
                val solution = Solution(solutionId, categoryId, scramble, Timing(start, end), penalty, "")
                solutions.add(solution)
            }
        } catch (e: SQLException) {
            throw DatabaseException(e)
        }
        val solutionArray = arrayOfNulls<Solution>(solutions.size)
        solutions.toArray(solutionArray)
        return solutionArray
    }

    fun update(solution: Solution) {
        try {
            val statement = connection.prepareStatement(
                    "UPDATE SOLUTION SET END = ?, PENALTY = ?, COMMENT = ? WHERE SOLUTION_ID = ?")
            statement.setTimestamp(1, Timestamp(solution.timing.end!!.time))
            statement.setString(2, solution.penalty)
            statement.setString(3, solution.comment)
            statement.setString(4, solution.solutionId.toString())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            throw DatabaseException(e)
        }
    }

    fun delete(solution: Solution) {
        try {
            val statement = connection.prepareStatement(
                    "DELETE FROM SOLUTION WHERE SOLUTION_ID = ?")
            statement.setString(1, solution.solutionId.toString())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            throw DatabaseException(e)
        }
    }
}