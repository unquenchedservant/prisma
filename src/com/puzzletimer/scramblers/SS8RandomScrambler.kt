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

class SS8RandomScrambler(scramblerInfo: ScramblerInfo, scrambleLength: Int) : BigCubeRandomScrambler(scramblerInfo, scrambleLength) {
    override val dimension: Int
        get() = 8
    override val moves: Array<Array<String>>
        get() = arrayOf(arrayOf("U", "U2", "U'"), arrayOf("2U", "2U2", "2U'"), arrayOf("3U", "3U2", "3U'"), arrayOf("4U", "4U2", "4U'"), arrayOf("3D", "3D2", "3D'"), arrayOf("2D", "2D2", "2D'"), arrayOf("D", "D2", "D'"), arrayOf("L", "L2", "L'"), arrayOf("2L", "2L2", "2L'"), arrayOf("3L", "3L2", "3L'"), arrayOf("4L", "4L2", "4L'"), arrayOf("3R", "3R2", "3R'"), arrayOf("2R", "2R2", "2R'"), arrayOf("R", "R2", "R'"), arrayOf("F", "F2", "F'"), arrayOf("2F", "2F2", "2F'"), arrayOf("3F", "3F2", "3F'"), arrayOf("4F", "4F2", "4F'"), arrayOf("3B", "3B2", "3B'"), arrayOf("2B", "2B2", "2B'"), arrayOf("B", "B2", "B'"))
}