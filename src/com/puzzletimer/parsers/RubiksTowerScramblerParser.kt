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

class RubiksTowerScramblerParser : ScrambleParser {
    override val puzzleId: String
        get() = "RUBIKS-TOWER"

    override fun parse(input: String): Array<String?> {
        val parser = Parser(input)
        val moves = ArrayList<String>()
        while (true) {
            parser.skipSpaces()
            var move = ""
            val face = parser.anyChar("BDFLRU")
            move += face ?: break
            val wide = parser.anyChar("w")
            if (wide != null) {
                move += wide
            }
            val suffix = parser.anyChar("2\'")
            if (suffix != null) {
                move += suffix
            }
            moves.add(move)
        }
        if (!isValidScramble(moves)) {
            return arrayOfNulls(0)
        }
        val movesArray = arrayOfNulls<String>(moves.size)
        moves.toArray(movesArray)
        return movesArray
    }

    private inner class State(var edgesPermutation: ByteArray, var edgesOrientation: ByteArray) {
        fun multiply(move: State?): State {
            val edgesPermutation = ByteArray(8)
            val edgesOrientation = ByteArray(8)
            for (i in 0..7) {
                edgesPermutation[i] = this.edgesPermutation[move!!.edgesPermutation[i].toInt()]
                edgesOrientation[i] = ((this.edgesOrientation[move.edgesPermutation[i].toInt()] + move.edgesOrientation[i]) % 3).toByte()
            }
            return State(edgesPermutation, edgesOrientation)
        }
    }

    private fun isValidScramble(moves: ArrayList<String>): Boolean {
        val moveUw: State = State(byteArrayOf(3, 0, 1, 2, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
        val moveDw: State = State(byteArrayOf(0, 1, 2, 3, 5, 6, 7, 4), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
        val moveLw: State = State(byteArrayOf(4, 1, 2, 0, 7, 5, 6, 3), byteArrayOf(2, 0, 0, 1, 1, 0, 0, 2))
        val moveRw: State = State(byteArrayOf(0, 2, 6, 3, 4, 1, 5, 7), byteArrayOf(0, 1, 2, 0, 0, 2, 1, 0))
        val moveFw: State = State(byteArrayOf(0, 1, 3, 7, 4, 5, 2, 6), byteArrayOf(0, 0, 1, 2, 0, 0, 2, 1))
        val moveBw: State = State(byteArrayOf(1, 5, 2, 3, 0, 4, 6, 7), byteArrayOf(1, 2, 0, 0, 2, 1, 0, 0))
        val moveTable = HashMap<String, State>()
        moveTable["Uw"] = moveUw
        moveTable["Uw2"] = moveUw.multiply(moveUw)
        moveTable["Uw'"] = moveUw.multiply(moveUw).multiply(moveUw)
        moveTable["Dw"] = moveDw
        moveTable["Dw2"] = moveDw.multiply(moveDw)
        moveTable["Dw'"] = moveDw.multiply(moveDw).multiply(moveDw)
        moveTable["Lw"] = moveLw
        moveTable["Lw2"] = moveLw.multiply(moveLw)
        moveTable["Lw'"] = moveLw.multiply(moveLw).multiply(moveLw)
        moveTable["Rw"] = moveRw
        moveTable["Rw2"] = moveRw.multiply(moveRw)
        moveTable["Rw'"] = moveRw.multiply(moveRw).multiply(moveRw)
        moveTable["Fw"] = moveFw
        moveTable["Fw2"] = moveFw.multiply(moveFw)
        moveTable["Fw'"] = moveFw.multiply(moveFw).multiply(moveFw)
        moveTable["Bw"] = moveBw
        moveTable["Bw2"] = moveBw.multiply(moveBw)
        moveTable["Bw'"] = moveBw.multiply(moveBw).multiply(moveBw)
        var state: State = State(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
        for (move in moves) {
            var validOrientation: ByteArray? = null
            if (move == "U" || move == "U2" || move == "U'") {
                validOrientation = byteArrayOf(0, 0, 0, 0, -1, -1, -1, -1)
            }
            if (move == "D" || move == "D2" || move == "D'") {
                validOrientation = byteArrayOf(-1, -1, -1, -1, 0, 0, 0, 0)
            }
            if (move == "L" || move == "L2" || move == "R'") {
                validOrientation = byteArrayOf(1, -1, -1, 2, 2, -1, -1, 1)
            }
            if (move == "R" || move == "R2" || move == "R'") {
                validOrientation = byteArrayOf(-1, 2, 1, -1, -1, 1, 2, -1)
            }
            if (move == "F" || move == "F2" || move == "F'") {
                validOrientation = byteArrayOf(-1, -1, 2, 1, -1, -1, 1, 2)
            }
            if (move == "B" || move == "B2" || move == "B'") {
                validOrientation = byteArrayOf(2, 1, -1, -1, 1, 2, -1, -1)
            }
            if (validOrientation != null) {
                for (i in state.edgesOrientation.indices) {
                    if (validOrientation[i] != -1 && state.edgesOrientation[i] != validOrientation[i]) {
                        return false
                    }
                }
            } else {
                state = state.multiply(moveTable[move])
            }
        }
        return true
    }
}