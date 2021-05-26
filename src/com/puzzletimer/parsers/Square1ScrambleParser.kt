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

class Square1ScrambleParser : ScrambleParser {
    override val puzzleId: String
        get() = "SQUARE-1"

    override fun parse(input: String): Array<String?> {
        val parser = Parser(input)
        var moves = ArrayList<String>()
        while (true) {
            parser.skipSpaces()

            // twist
            val twist = parser.string("/")
            if (twist != null) {
                moves.add(twist)
                continue
            }

            // top/bottom turn
            val open = parser.string("(")
            if (open != null) {
                var move = open
                parser.skipSpaces()
                val top = parser.number()
                move += top ?: break
                parser.skipSpaces()
                val comma = parser.string(",")
                move += comma ?: break
                parser.skipSpaces()
                val bottom = parser.number()
                move += bottom ?: break
                parser.skipSpaces()
                val close = parser.string(")")
                move += close ?: break
                moves.add(move)
                continue
            }
            break
        }
        moves = fixImplicitTwists(moves)
        if (!isValidScramble(moves)) {
            return arrayOfNulls(0)
        }
        val movesArray = arrayOfNulls<String>(moves.size)
        moves.toArray(movesArray)
        return movesArray
    }

    private fun fixImplicitTwists(sequence: ArrayList<String>): ArrayList<String> {
        var implicit = true
        for (move in sequence) {
            if (move == "/") {
                implicit = false
            }
        }
        if (!implicit) {
            return sequence
        }
        val newSequence = ArrayList<String>()
        for (move in sequence) {
            newSequence.add(move)
            newSequence.add("/")
        }
        return newSequence
    }

    private fun isValidScramble(sequence: ArrayList<String>): Boolean {
        var state = Square1ShapeSolver.State.id
        for (move in sequence) {
            if (move == "/") {
                if (!state!!.isTwistable) {
                    return false
                }
            }
            state = state!!.applyMove(move)
        }
        return true
    }
}