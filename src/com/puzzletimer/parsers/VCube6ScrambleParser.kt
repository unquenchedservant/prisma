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

class VCube6ScrambleParser : ScrambleParser {
    override val puzzleId: String
        get() = "6x6x6-CUBE"

    override fun parse(input: String): Array<String?> {
        val parser = Parser(input)
        val moves = ArrayList<String>()
        while (true) {
            parser.skipSpaces()
            var move = ""
            val slice = parser.anyChar("23")
            if (slice != null) {
                move += slice
            }
            val face = parser.anyChar("BDFLRU")
            move += face ?: break
            val suffix = parser.anyChar("2\'")
            if (suffix != null) {
                move += suffix
                if (suffix == "2") {
                    // ignore prime
                    parser.string("\'")
                }
            }
            moves.add(move)
        }
        val movesArray = arrayOfNulls<String>(moves.size)
        moves.toArray(movesArray)
        return movesArray
    }
}