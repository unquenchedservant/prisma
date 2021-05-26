package com.puzzletimer.tips

import com.puzzletimer.tips.Tip
import com.puzzletimer.models.Scramble
import com.puzzletimer.solvers.RubiksCubeSolver
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.HashMap

class RubiksCubeClassicPochmannEdges : Tip {
    override val tipId: String
        get() = "RUBIKS-CUBE-CLASSIC-POCHMANN-EDGES"
    override val puzzleId: String
        get() = "RUBIKS-CUBE"
    override val tipDescription: String
        get() = _("tip.RUBIKS-CUBE-CLASSIC-POCHMANN-EDGES")

    override fun getTip(scramble: Scramble?): String? {
        val state = RubiksCubeSolver.State.id.applySequence(scramble!!.sequence)

        // pieces already solved
        val solved = BooleanArray(12)
        for (i in solved.indices) {
            solved[i] = i == 5 || state.edgesPermutation[i] == i && state.edgesOrientation[i] == 0
        }

        // sticker sequence
        val stickerSequence = ArrayList<String>()
        val stickerNames = arrayOf(arrayOf("BL", "LB"), arrayOf("BR", "RB"), arrayOf("FR", "RF"), arrayOf("FL", "LF"), arrayOf("UB", "BU"), arrayOf("UR", "RU"), arrayOf("UF", "FU"), arrayOf("UL", "LU"), arrayOf("DB", "BD"), arrayOf("DR", "RD"), arrayOf("DF", "FD"), arrayOf("DL", "LD"))
        var cycleFirstPiece = 5
        var currentPermutation = 5
        var currentOrientation = 0
        while (true) {
            var nextPermutation = state.edgesPermutation[currentPermutation].toInt()
            var nextOrientation = (2 - state.edgesOrientation[currentPermutation] + currentOrientation) % 2

            // break into a new cycle
            if (nextPermutation == cycleFirstPiece) {
                if (cycleFirstPiece != 5) {
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
        tip.append(_("tip.RUBIKS-CUBE-CLASSIC-POCHMANN-EDGES")).append(":\n")
        tip.append("  [T1]  R U R' U' R' F R2 U' R' U' R U R' F'\n")
        tip.append("  [T2]  x' R2 U' R' U x R' F' U' F R U R' U'\n")
        tip.append("  [J1]  R U R' F' R U R' U' R' F R2 U' R' U'\n")
        tip.append("  [J2]  R' U2 R U R' U2 L U' R U L'\n")
        tip.append("\n")
        val letteringScheme = HashMap<String, String>()
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
        val solutions = HashMap<String, String>()
        solutions["UB"] = "[J2]"
        solutions["BU"] = "l [J1] l'"
        solutions["UF"] = "[J1]"
        solutions["FU"] = "l' [J2] l"
        solutions["UL"] = "[T1]"
        solutions["LU"] = "[T2]"
        solutions["BL"] = "L [T1] L'"
        solutions["LB"] = "L [T2] L'"
        solutions["BR"] = "d2 L' [T1] L d2"
        solutions["RB"] = "d L [T1] L' d'"
        solutions["FR"] = "d2 L [T1] L' d2"
        solutions["RF"] = "d' L' [T1] L d"
        solutions["FL"] = "L' [T1] L"
        solutions["LF"] = "L' [T2] L"
        solutions["DB"] = "l2 [J1] l2"
        solutions["BD"] = "l [J2] l'"
        solutions["DR"] = "S' [T1] S"
        solutions["RD"] = "D' l' [J1] l D"
        solutions["DF"] = "l2 [J2] l2"
        solutions["FD"] = "l' [J1] l"
        solutions["DL"] = "L2 [T1] L2"
        solutions["LD"] = "L2 [T2] L2"
        for (sticker in stickerSequence) {
            tip.append("  (UR ").append(sticker).append(") ").append(letteringScheme[sticker]).append("  ").append(solutions[sticker]).append("\n")
        }
        return tip.toString().trim { it <= ' ' }
    }

    override fun toString(): String {
        return tipDescription
    }
}