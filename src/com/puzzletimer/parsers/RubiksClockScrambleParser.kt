package com.puzzletimer.parsers

import com.puzzletimer.solvers.Square1ShapeSolver.State.isTwistable
import com.puzzletimer.solvers.Square1ShapeSolver.State.applyMove
import com.puzzletimer.parsers.ScrambleParser
import java.util.HashMap
import com.puzzletimer.parsers.EmptyScrambleParser
import com.puzzletimer.parsers.FloppyCubeScrambleParser
import com.puzzletimer.parsers.MegaminxScrambleParser
import com.puzzletimer.parsers.ProfessorsCubeScrambleParser
import com.puzzletimer.parsers.PyraminxScrambleParser
import com.puzzletimer.parsers.RubiksClockScrambleParser
import com.puzzletimer.parsers.RubiksCubeScrambleParser
import com.puzzletimer.parsers.RubiksDominoScrambleParser
import com.puzzletimer.parsers.RubiksPocketCubeScrambleParser
import com.puzzletimer.parsers.RubiksRevengeScramblerParser
import com.puzzletimer.parsers.RubiksTowerScramblerParser
import com.puzzletimer.parsers.SkewbScrambleParser
import com.puzzletimer.parsers.Square1ScrambleParser
import com.puzzletimer.parsers.TowerCubeScrambleParser
import com.puzzletimer.parsers.VCube6ScrambleParser
import com.puzzletimer.parsers.VCube7ScrambleParser
import com.puzzletimer.parsers.SS9ScrambleParser
import com.puzzletimer.parsers.SS8ScrambleParser
import com.puzzletimer.solvers.Square1ShapeSolver
import java.util.ArrayList

class RubiksClockScrambleParser : ScrambleParser {
    override val puzzleId: String
        get() = "RUBIKS-CLOCK"

    override fun parse(input: String): Array<String?> {
        val parser = Parser(input)
        val moves = ArrayList<String?>()
        while (true) {
            parser.skipSpaces()
            var move: String? = ""
            val pin1 = parser.anyChar("Ud")
            move += pin1 ?: break
            val pin2 = parser.anyChar("Ud")
            move += pin2 ?: break
            val pin3 = parser.anyChar("Ud")
            move += pin3 ?: break
            val pin4 = parser.anyChar("Ud")
            move += pin4 ?: break
            parser.skipSpaces()
            val wheel1 = parser.anyChar("ud")
            val isValidWheel1 = wheel1 != null && (wheel1 == pin1.toLowerCase() || wheel1 == pin2.toLowerCase() || wheel1 == pin3.toLowerCase() || wheel1 == pin4.toLowerCase())
            if (isValidWheel1) {
                move += " $wheel1"
                parser.skipSpaces()
                val equals1 = parser.string("=")
                move += equals1 ?: break
                parser.skipSpaces()
                val negative1 = parser.string("-")
                if (negative1 != null) {
                    move += negative1
                }
                val turns1 = parser.number()
                move += turns1 ?: break
                parser.skipSpaces()
                val comma = parser.string(",")
                if (comma != null) {
                    move += comma
                    parser.skipSpaces()
                    val wheel2 = parser.anyChar("ud")
                    val isValidWheel2 = wheel2 != null &&
                            wheel2 != wheel1 && (wheel2 == pin1.toLowerCase() || wheel2 == pin2.toLowerCase() || wheel2 == pin3.toLowerCase() || wheel2 == pin4.toLowerCase())
                    move += if (isValidWheel2) {
                        wheel2
                    } else {
                        break
                    }
                    parser.skipSpaces()
                    val equals2 = parser.string("=")
                    move += equals2 ?: break
                    parser.skipSpaces()
                    val negative2 = parser.string("-")
                    if (negative2 != null) {
                        move += negative2
                    }
                    val turns2 = parser.number()
                    move += turns2 ?: break
                }
            }
            parser.skipSpaces()
            parser.string("/")
            moves.add(move)
        }
        val movesArray = arrayOfNulls<String>(moves.size)
        moves.toArray(movesArray)
        return movesArray
    }
}