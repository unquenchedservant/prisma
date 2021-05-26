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
import java.text.DateFormat
import java.text.SimpleDateFormat

class DatabaseException(val sqlException: SQLException) : RuntimeException() {
    override val message: String
        get() = sqlException.message!!
}