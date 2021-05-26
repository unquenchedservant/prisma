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
import com.puzzletimer.solvers.FloppyCubeSolver
import com.puzzletimer.scramblers.BigCubeRandomScrambler
import com.puzzletimer.solvers.PyraminxSolver
import com.puzzletimer.solvers.RubiksClockSolver
import com.puzzletimer.scramblers.RubiksCubeRandomScrambler
import com.puzzletimer.solvers.RubiksCubeCrossSolver
import com.puzzletimer.solvers.RubiksCubeSolver
import com.puzzletimer.solvers.RubiksCubeRUSolver
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
import java.util.*

abstract class BigCubeRandomScrambler(override val scramblerInfo: ScramblerInfo, private val scrambleLength: Int) : Scrambler {
    private val random: Random
    abstract val dimension: Int
    abstract val moves: Array<Array<String>>
    // if not in the same axis

    // sort moves in the same axis
    override val nextScramble: Scramble
        get() {
            val slice = IntArray(scrambleLength)
            val power = IntArray(scrambleLength)
            var i = 0
            while (i < scrambleLength) {
                val s = random.nextInt(moves.size)
                val p = random.nextInt(3)
                var ignore = false
                for (j in i - 1 downTo 0) {
                    // if not in the same axis
                    if (s / (dimension - 1) != slice[j] / (dimension - 1)) {
                        break
                    }
                    if (s == slice[j]) {
                        ignore = true
                    }
                }
                if (!ignore) {
                    slice[i] = s
                    power[i] = p
                    i++
                }
            }

            // sort moves in the same axis
            i = 0
            while (i < slice.size) {
                var len = 1
                while (i + len < slice.size && slice[i] / (dimension - 1) == slice[i + len] / (dimension - 1)) {
                    len++
                }
                Arrays.sort(slice, i, i + len)
                i += len
            }
            val moves = moves
            val sequence = arrayOfNulls<String>(scrambleLength)
            for (j in sequence.indices) {
                sequence[j] = moves[slice[j]][power[j]]
            }
            return Scramble(
                    scramblerInfo.scramblerId,
                    sequence)
        }

    override fun toString(): String {
        return scramblerInfo.description
    }

    init {
        random = Random()
    }
}