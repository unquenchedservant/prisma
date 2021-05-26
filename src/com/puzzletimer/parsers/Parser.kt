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

class Parser(input: String) {
    private val input: CharArray
    private var pos: Int
    fun skipSpaces() {
        while (pos < input.size && Character.isWhitespace(input[pos])) {
            pos++
        }
    }

    fun anyChar(chars: String): String? {
        if (pos < input.size && chars.contains(Character.toString(input[pos]))) {
            pos++
            return Character.toString(input[pos - 1])
        }
        return null
    }

    fun string(s: String): String? {
        val sChars = s.toCharArray()
        var p = pos
        for (i in sChars.indices) {
            if (p < input.size && sChars[i] == input[p]) {
                p++
            } else {
                return null
            }
        }
        pos = p
        return s
    }

    fun number(): String? {
        var result = ""
        var p = pos
        if (input[p] == '-') {
            result += input[p]
            p++
        }
        var nDigits = 0
        while (p < input.size && Character.isDigit(input[p])) {
            nDigits++
            result += input[p]
            p++
        }
        if (nDigits <= 0) {
            return null
        }
        pos = p
        return result
    }

    init {
        this.input = input.toCharArray()
        pos = 0
    }
}