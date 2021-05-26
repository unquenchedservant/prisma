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

class RubiksCubeEasyCrossScrambler(override val scramblerInfo: ScramblerInfo, private val maxDistance: Int) : Scrambler {
    private val rubiksCubeRandomScrambler: RubiksCubeRandomScrambler
    override val nextScramble: Scramble
        get() {
            val x = RubiksCubeSolver.State(byteArrayOf(3, 2, 6, 7, 0, 1, 5, 4), byteArrayOf(2, 1, 2, 1, 1, 2, 1, 2), byteArrayOf(7, 5, 9, 11, 6, 2, 10, 3, 4, 1, 8, 0), byteArrayOf(0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0))
            while (true) {
                val state = rubiksCubeRandomScrambler.randomState
                val solution = solve(
                        x.multiply(x).multiply(state).multiply(x).multiply(x))
                if (solution[0].length <= maxDistance) {
                    return Scramble(
                            scramblerInfo.scramblerId,
                            generate(state!!))
                }
            }
        }

    override fun toString(): String {
        return scramblerInfo.description
    }

    init {
        rubiksCubeRandomScrambler = RubiksCubeRandomScrambler(
                scramblerInfo, byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))
    }
}