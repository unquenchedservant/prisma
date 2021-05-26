package com.puzzletimer.scramblers

import com.puzzletimer.solvers.FloppyCubeSolver.generate
import com.puzzletimer.solvers.FloppyCubeSolver.getRandomState
import com.puzzletimer.solvers.PyraminxSolver.generate
import com.puzzletimer.solvers.PyraminxSolver.getRandomState
import com.puzzletimer.solvers.RubiksClockSolver.getRandomSequence
import com.puzzletimer.solvers.RubiksCubeCrossSolver.solve
import com.puzzletimer.solvers.RubiksCubeSolver.State.multiply
import com.puzzletimer.solvers.RubiksCubeSolver.generate
import com.puzzletimer.solvers.RubiksCubeRUSolver.generate
import com.puzzletimer.solvers.RubiksCubeRUSolver.getRandomState
import com.puzzletimer.solvers.RubiksDominoSolver.generate
import com.puzzletimer.solvers.RubiksDominoSolver.getRandomState
import com.puzzletimer.solvers.RubiksPocketCubeSolver.generate
import com.puzzletimer.solvers.RubiksPocketCubeSolver.getRandomState
import com.puzzletimer.solvers.RubiksTowerSolver.generate
import com.puzzletimer.solvers.RubiksTowerSolver.getRandomState
import com.puzzletimer.solvers.SkewbSolver.generate
import com.puzzletimer.solvers.SkewbSolver.getRandomState
import com.puzzletimer.solvers.Square1Solver.generate
import com.puzzletimer.solvers.Square1Solver.getRandomState
import com.puzzletimer.solvers.TowerCubeSolver.generate
import com.puzzletimer.solvers.TowerCubeSolver.getRandomState
import com.puzzletimer.models.ScramblerInfo
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.models.Scramble
import java.util.Arrays
import com.puzzletimer.solvers.FloppyCubeSolver
import com.puzzletimer.scramblers.BigCubeRandomScrambler
import com.puzzletimer.solvers.PyraminxSolver
import com.puzzletimer.solvers.RubiksClockSolver
import com.puzzletimer.scramblers.RubiksCubeRandomScrambler
import com.puzzletimer.solvers.RubiksCubeCrossSolver
import com.puzzletimer.solvers.RubiksCubeSolver
import java.util.HashMap
import com.puzzletimer.solvers.RubiksCubeRUSolver
import java.util.Collections
import com.puzzletimer.solvers.RubiksDominoSolver
import com.puzzletimer.solvers.RubiksPocketCubeSolver
import com.puzzletimer.solvers.RubiksTowerSolver
import com.puzzletimer.scramblers.EmptyScrambler
import com.puzzletimer.scramblers.RubiksPocketCubeRandomScrambler
import com.puzzletimer.scramblers.RubiksCubeLUScrambler
import com.puzzletimer.scramblers.RubiksCubeRUScrambler
import com.puzzletimer.scramblers.RubiksCubeSingleStickerCycleScrambler
import com.puzzletimer.scramblers.RubiksCubeEasyCrossScrambler
import com.puzzletimer.scramblers.RubiksRevengeRandomScrambler
import com.puzzletimer.scramblers.ProfessorsCubeRandomScrambler
import com.puzzletimer.scramblers.VCube6RandomScrambler
import com.puzzletimer.scramblers.VCube7RandomScrambler
import com.puzzletimer.scramblers.SS8RandomScrambler
import com.puzzletimer.scramblers.SS9RandomScrambler
import com.puzzletimer.scramblers.RubiksClockRandomScrambler
import com.puzzletimer.scramblers.MegaminxRandomScrambler
import com.puzzletimer.scramblers.PyraminxRandomScrambler
import com.puzzletimer.scramblers.Square1RandomScrambler
import com.puzzletimer.scramblers.Square1CubeShapeScrambler
import com.puzzletimer.scramblers.SkewbRandomScrambler
import com.puzzletimer.scramblers.FloppyCubeRandomScrambler
import com.puzzletimer.scramblers.TowerCubeRandomScrambler
import com.puzzletimer.scramblers.RubiksTowerRandomScrambler
import com.puzzletimer.scramblers.RubiksDominoRandomScrambler
import com.puzzletimer.solvers.SkewbSolver
import com.puzzletimer.solvers.Square1Solver
import com.puzzletimer.solvers.TowerCubeSolver

class RubiksCubeSingleStickerCycleScrambler(override val scramblerInfo: ScramblerInfo) : Scrambler {
    private val rubiksCubeRandomScrambler: RubiksCubeRandomScrambler// end of cycle// end of cycle

    // edges
    // corners
    override val nextScramble: Scramble
        get() {
            var state: RubiksCubeSolver.State? = null
            var singleCornerCycle = false
            var singleEdgeCycle = false
            while (!singleCornerCycle || !singleEdgeCycle) {
                state = rubiksCubeRandomScrambler.randomState

                // corners
                val solvedCorners = BooleanArray(8)
                for (i in solvedCorners.indices) {
                    solvedCorners[i] = state!!.cornersPermutation!![i] == i &&
                            state.cornersOrientation!![i] == 0
                }
                solvedCorners[0] = true
                var currentCorner = 0
                while (true) {
                    val nextCorner = state!!.cornersPermutation!![currentCorner].toInt()

                    // end of cycle
                    if (nextCorner == 0) {
                        var solved = true
                        for (i in solvedCorners.indices) {
                            if (!solvedCorners[i]) {
                                solved = false
                            }
                        }
                        singleCornerCycle = solved
                        break
                    }
                    currentCorner = nextCorner
                    solvedCorners[currentCorner] = true
                }

                // edges
                val solvedEdges = BooleanArray(12)
                for (i in solvedEdges.indices) {
                    solvedEdges[i] = state.edgesPermutation!![i] == i &&
                            state.edgesOrientation!![i] == 0
                }
                solvedEdges[0] = true
                var currentEdge = 0
                while (true) {
                    val nextEdge = state.edgesPermutation!![currentEdge].toInt()

                    // end of cycle
                    if (nextEdge == 0) {
                        var solved = true
                        for (i in solvedEdges.indices) {
                            if (!solvedEdges[i]) {
                                solved = false
                            }
                        }
                        singleEdgeCycle = solved
                        break
                    }
                    currentEdge = nextEdge
                    solvedEdges[currentEdge] = true
                }
            }
            return Scramble(
                    scramblerInfo.scramblerId,
                    generate(state!!))
        }

    override fun toString(): String {
        return scramblerInfo.description
    }

    init {
        rubiksCubeRandomScrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-RANDOM", "RUBIKS-CUBE", "Random scrambler"), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))
    }
}