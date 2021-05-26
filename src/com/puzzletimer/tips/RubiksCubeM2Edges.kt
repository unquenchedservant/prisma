package com.puzzletimer.tips

import com.puzzletimer.tips.Tip
import com.puzzletimer.models.Scramble
import com.puzzletimer.solvers.RubiksCubeSolver
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.HashMap

class RubiksCubeM2Edges : Tip {
    override val tipId: String
        get() = "RUBIKS-CUBE-M2-EDGES"
    override val puzzleId: String
        get() = "RUBIKS-CUBE"
    override val tipDescription: String
        get() = _("tip.RUBIKS-CUBE-M2-EDGES")

    override fun getTip(scramble: Scramble?): String? {
        val state = RubiksCubeSolver.State.id.applySequence(scramble!!.sequence)

        // pieces already solved
        val solved = BooleanArray(12)
        for (i in solved.indices) {
            solved[i] = i == 10 || state.edgesPermutation[i] == i && state.edgesOrientation[i] == 0
        }

        // sticker sequence
        val stickerSequence = ArrayList<String>()
        val stickerNames = arrayOf(arrayOf("BL", "LB"), arrayOf("BR", "RB"), arrayOf("FR", "RF"), arrayOf("FL", "LF"), arrayOf("UB", "BU"), arrayOf("UR", "RU"), arrayOf("UF", "FU"), arrayOf("UL", "LU"), arrayOf("DB", "BD"), arrayOf("DR", "RD"), arrayOf("DF", "FD"), arrayOf("DL", "LD"))
        var cycleFirstPiece = 10
        var currentPermutation = 10
        var currentOrientation = 0
        while (true) {
            var nextPermutation = state.edgesPermutation[currentPermutation].toInt()
            var nextOrientation = (2 - state.edgesOrientation[currentPermutation] + currentOrientation) % 2

            // break into a new cycle
            if (nextPermutation == cycleFirstPiece) {
                if (cycleFirstPiece != 10) {
                    stickerSequence.add(stickerNames[nextPermutation][nextOrientation])
                }
                var allPiecesSolved = true
                for (i in 0..11) {
                    if (!solved[i]) {
                        cycleFirstPiece = i
                        currentPermutation = i
                        currentOrientation = 0
                        nextPermutation = i
                        nextOrientation = 0
                        allPiecesSolved = false
                        break
                    }
                }
                if (allPiecesSolved) {
                    break
                }
            }
            stickerSequence.add(stickerNames[nextPermutation][nextOrientation])
            currentPermutation = nextPermutation
            currentOrientation = nextOrientation
            solved[currentPermutation] = true
        }

        // solution
        val tip = StringBuilder()
        tip.append(_("tip.RUBIKS-CUBE-M2-EDGES")).append(":\n")
        val letteringScheme = HashMap<String?, String>()
        letteringScheme["UB"] = "A"
        letteringScheme["UR"] = "B"
        letteringScheme["UF"] = "C"
        letteringScheme["UL"] = "D"
        letteringScheme["LU"] = "E"
        letteringScheme["LF"] = "F"
        letteringScheme["LD"] = "G"
        letteringScheme["LB"] = "H"
        letteringScheme["FU"] = "I"
        letteringScheme["FR"] = "J"
        letteringScheme["FD"] = "K"
        letteringScheme["FL"] = "L"
        letteringScheme["RU"] = "M"
        letteringScheme["RB"] = "N"
        letteringScheme["RD"] = "O"
        letteringScheme["RF"] = "P"
        letteringScheme["BU"] = "Q"
        letteringScheme["BL"] = "R"
        letteringScheme["BD"] = "S"
        letteringScheme["BR"] = "T"
        letteringScheme["DF"] = "U"
        letteringScheme["DR"] = "V"
        letteringScheme["DB"] = "W"
        letteringScheme["DL"] = "X"
        val solutions = HashMap<String?, String>()
        solutions["UB"] = "M2"
        solutions["UR"] = "R' U R U' M2 U R' U' R"
        solutions["UF"] = "U2 M' U2 M'"
        solutions["UL"] = "L U' L' U M2 U' L U L'"
        solutions["LU"] = "x' U L' U' M2 U L U' x"
        solutions["LF"] = "x' U L2' U' M2 U L2 U' x"
        solutions["LD"] = "x' U L U' M2 U L' U' x"
        solutions["LB"] = "r' U L U' M2 U L' U' r"
        solutions["FU"] = "F E R U R' E' R U' R' F' M2"
        solutions["FR"] = "U R U' M2 U R' U'"
        solutions["FL"] = "U' L' U M2 U' L U"
        solutions["RU"] = "x' U' R U M2 U' R' U x"
        solutions["RB"] = "l U' R' U M2 U' R U l'"
        solutions["RD"] = "x' U' R' U M2 U' R U x"
        solutions["RF"] = "x' U' R2 U M2 U' R2 U x"
        solutions["BU"] = "F' D R' F D' M2 D F' R D' F"
        solutions["BL"] = "U' L U M2 U' L' U"
        solutions["BD"] = "M2 D R' U R' U' M' U R U' M R D'"
        solutions["BR"] = "U R' U' M2 U R U'"
        solutions["DR"] = "U R2 U' M2 U R2 U'"
        solutions["DB"] = "M U2 M U2"
        solutions["DL"] = "U' L2 U M2 U' L2 U"
        val mLayerInverse = HashMap<String?, String>()
        mLayerInverse["FU"] = "BD"
        mLayerInverse["UF"] = "DB"
        mLayerInverse["BD"] = "FU"
        mLayerInverse["DB"] = "UF"
        for (i in stickerSequence.indices) {
            var sticker: String? = stickerSequence[i]
            tip.append("  (DF ").append(sticker).append(") ").append(letteringScheme[sticker])
            if (i % 2 == 1 && mLayerInverse.containsKey(sticker)) {
                sticker = mLayerInverse[sticker]
            }
            tip.append("  ").append(solutions[sticker]).append("\n")
        }
        return tip.toString().trim { it <= ' ' }
    }

    override fun toString(): String {
        return tipDescription
    }
}