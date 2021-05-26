package com.puzzletimer.tips

import com.puzzletimer.tips.Tip
import com.puzzletimer.models.Scramble
import com.puzzletimer.solvers.RubiksCubeSolver
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.HashMap

class RubiksCubeClassicPochmannCorners : Tip {
    override val tipId: String
        get() = "RUBIKS-CUBE-CLASSIC-POCHMANN-CORNERS"
    override val puzzleId: String
        get() = "RUBIKS-CUBE"
    override val tipDescription: String
        get() = _("tip.RUBIKS-CUBE-CLASSIC-POCHMANN-CORNERS")

    override fun getTip(scramble: Scramble?): String? {
        val state = RubiksCubeSolver.State.id.applySequence(scramble!!.sequence)

        // pieces already solved
        val solved = BooleanArray(8)
        for (i in solved.indices) {
            solved[i] = i == 0 || state.cornersPermutation[i] == i && state.cornersOrientation[i] == 0
        }

        // sticker sequence
        val stickerSequence = ArrayList<String>()
        val stickerNames = arrayOf(arrayOf("ULB", "LBU", "BUL"), arrayOf("UBR", "BRU", "RUB"), arrayOf("URF", "RFU", "FUR"), arrayOf("UFL", "FLU", "LUF"), arrayOf("DBL", "BLD", "LDB"), arrayOf("DRB", "RBD", "BDR"), arrayOf("DFR", "FRD", "RDF"), arrayOf("DLF", "LFD", "FDL"))
        var cycleFirstPiece = 0
        var currentPermutation = 0
        var currentOrientation = 1
        while (true) {
            var nextPermutation = state.cornersPermutation[currentPermutation].toInt()
            var nextOrientation = (3 - state.cornersOrientation[currentPermutation] + currentOrientation) % 3

            // break into a new cycle
            if (nextPermutation == cycleFirstPiece) {
                if (cycleFirstPiece != 0) {
                    stickerSequence.add(stickerNames[nextPermutation][nextOrientation])
                }
                var allPiecesSolved = true
                for (i in 0..7) {
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
        tip.append(_("tip.RUBIKS-CUBE-CLASSIC-POCHMANN-CORNERS")).append(":\n")
        tip.append("  [Y]  R U' R' U' R U R' F' R U R' U' R' F R\n")
        tip.append("\n")
        val letteringScheme = HashMap<String, String>()
        letteringScheme["ULB"] = "A"
        letteringScheme["UBR"] = "B"
        letteringScheme["URF"] = "C"
        letteringScheme["UFL"] = "D"
        letteringScheme["LBU"] = "E"
        letteringScheme["LUF"] = "F"
        letteringScheme["LFD"] = "G"
        letteringScheme["LDB"] = "H"
        letteringScheme["FLU"] = "I"
        letteringScheme["FUR"] = "J"
        letteringScheme["FRD"] = "K"
        letteringScheme["FDL"] = "L"
        letteringScheme["RFU"] = "M"
        letteringScheme["RUB"] = "N"
        letteringScheme["RBD"] = "O"
        letteringScheme["RDF"] = "P"
        letteringScheme["BRU"] = "Q"
        letteringScheme["BUL"] = "R"
        letteringScheme["BLD"] = "S"
        letteringScheme["BDR"] = "T"
        letteringScheme["DLF"] = "U"
        letteringScheme["DFR"] = "V"
        letteringScheme["DRB"] = "W"
        letteringScheme["DBL"] = "X"
        val solutions = HashMap<String, String>()
        solutions["UBR"] = "R2 [Y] R2"
        solutions["BRU"] = "R D' [Y] D R'"
        solutions["RUB"] = "R' F [Y] F' R"
        solutions["URF"] = "R2 D' [Y] D R2"
        solutions["RFU"] = "F [Y] F'"
        solutions["FUR"] = "R' [Y] R"
        solutions["UFL"] = "F2 [Y] F2"
        solutions["FLU"] = "F R' [Y] R F'"
        solutions["LUF"] = "F' D [Y] D' F"
        solutions["DBL"] = "D2 [Y] D2"
        solutions["BLD"] = "D F' [Y] F D'"
        solutions["LDB"] = "D' R [Y] R' D"
        solutions["DRB"] = "D' [Y] D"
        solutions["RBD"] = "R2 F [Y] F' R2"
        solutions["BDR"] = "R [Y] R'"
        solutions["DFR"] = "[Y]"
        solutions["FRD"] = "F' R' [Y] R F"
        solutions["RDF"] = "R F [Y] F' R'"
        solutions["DLF"] = "D [Y] D'"
        solutions["LFD"] = "F' [Y] F"
        solutions["FDL"] = "F2 R' [Y] R F2"
        for (sticker in stickerSequence) {
            tip.append("  (LBU ").append(sticker).append(") ").append(letteringScheme[sticker]).append("  ").append(solutions[sticker]).append("\n")
        }
        return tip.toString().trim { it <= ' ' }
    }

    override fun toString(): String {
        return tipDescription
    }
}