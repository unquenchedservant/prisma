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

class MegaminxScrambleParser : ScrambleParser {
    override val puzzleId: String
        get() = "MEGAMINX"

    override fun parse(input: String): Array<String?> {
        val parser = Parser(input)
        val moves = ArrayList<String>()
        while (true) {
            parser.skipSpaces()
            var move = ""
            val face = parser.anyChar("UDR")
            move += face ?: break
            if (face == "U") {
                val prime = parser.anyChar("\'")
                if (prime != null) {
                    move += prime
                }
            } else {
                var orientation = parser.string("++")
                if (orientation == null) {
                    orientation = parser.string("--")
                }
                move += orientation ?: break
            }
            moves.add(move)
        }
        val movesArray = arrayOfNulls<String>(moves.size)
        moves.toArray(movesArray)
        return movesArray
    }
}