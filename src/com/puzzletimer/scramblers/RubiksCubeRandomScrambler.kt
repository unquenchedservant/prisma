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

class RubiksCubeRandomScrambler(
        override val scramblerInfo: ScramblerInfo,
        private val cornersPermutation: ByteArray,
        private val cornersOrientation: ByteArray,
        private val edgesPermutation: ByteArray,
        private val edgesOrientation: ByteArray) : Scrambler {
    private val random: Random
    private fun permutationSign(permutation: ByteArray): Int {
        var nInversions = 0
        for (i in permutation.indices) {
            for (j in i + 1 until permutation.size) {
                if (permutation[i] > permutation[j]) {
                    nInversions++
                }
            }
        }
        return if (nInversions % 2 == 0) 1 else -1
    }

    // corners permutation
    val randomState:

    // corners orientation

    // edges permutation

    // edges orientation
            RubiksCubeSolver.State
        get() {
            var cornersPermutation: ByteArray
            var cornersOrientation: ByteArray
            var edgesPermutation: ByteArray
            var edgesOrientation: ByteArray
            do {
                // corners permutation
                val undefinedCornersPermutation = ArrayList<Byte>()
                for (i in this.cornersPermutation.indices) {
                    undefinedCornersPermutation.add(i.toByte())
                }
                for (i in this.cornersPermutation.indices) {
                    if (this.cornersPermutation[i] >= 0) {
                        undefinedCornersPermutation.remove(this.cornersPermutation[i])
                    }
                }
                Collections.shuffle(undefinedCornersPermutation, random)
                cornersPermutation = ByteArray(this.cornersPermutation.size)
                for (i in cornersPermutation.indices) {
                    if (this.cornersPermutation[i] >= 0) {
                        cornersPermutation[i] = this.cornersPermutation[i]
                    } else {
                        cornersPermutation[i] = undefinedCornersPermutation[0]
                        undefinedCornersPermutation.removeAt(0)
                    }
                }

                // corners orientation
                var nUndefinedCornerOrientations = 0
                for (i in this.cornersOrientation.indices) {
                    if (this.cornersOrientation[i] < 0) {
                        nUndefinedCornerOrientations++
                    }
                }
                var cornersOrientationSum = 0
                cornersOrientation = ByteArray(this.cornersOrientation.size)
                for (i in cornersOrientation.indices) {
                    if (this.cornersOrientation[i] >= 0) {
                        cornersOrientation[i] = this.cornersOrientation[i]
                    } else {
                        if (nUndefinedCornerOrientations == 1) {
                            cornersOrientation[i] = ((3 - cornersOrientationSum) % 3).toByte()
                        } else {
                            cornersOrientation[i] = random.nextInt(3).toByte()
                        }
                        nUndefinedCornerOrientations--
                    }
                    cornersOrientationSum += cornersOrientation[i]
                    cornersOrientationSum %= 3
                }

                // edges permutation
                val undefinedEdgesPermutation = ArrayList<Byte>()
                for (i in this.edgesPermutation.indices) {
                    undefinedEdgesPermutation.add(i.toByte())
                }
                for (i in this.edgesPermutation.indices) {
                    if (this.edgesPermutation[i] >= 0) {
                        undefinedEdgesPermutation.remove(this.edgesPermutation[i])
                    }
                }
                Collections.shuffle(undefinedEdgesPermutation, random)
                edgesPermutation = ByteArray(this.edgesPermutation.size)
                for (i in edgesPermutation.indices) {
                    if (this.edgesPermutation[i] >= 0) {
                        edgesPermutation[i] = this.edgesPermutation[i]
                    } else {
                        edgesPermutation[i] = undefinedEdgesPermutation[0]
                        undefinedEdgesPermutation.removeAt(0)
                    }
                }

                // edges orientation
                var nUndefinedEdgeOrientations = 0
                for (i in this.edgesOrientation.indices) {
                    if (this.edgesOrientation[i] < 0) {
                        nUndefinedEdgeOrientations++
                    }
                }
                var edgesOrientationSum = 0
                edgesOrientation = ByteArray(this.edgesOrientation.size)
                for (i in edgesOrientation.indices) {
                    if (this.edgesOrientation[i] >= 0) {
                        edgesOrientation[i] = this.edgesOrientation[i]
                    } else {
                        if (nUndefinedEdgeOrientations == 1) {
                            edgesOrientation[i] = ((2 - edgesOrientationSum) % 2).toByte()
                        } else {
                            edgesOrientation[i] = random.nextInt(2).toByte()
                        }
                        nUndefinedEdgeOrientations--
                    }
                    edgesOrientationSum += edgesOrientation[i]
                    edgesOrientationSum %= 2
                }
            } while (permutationSign(cornersPermutation) != permutationSign(edgesPermutation))
            return RubiksCubeSolver.State(
                    cornersPermutation,
                    cornersOrientation,
                    edgesPermutation,
                    edgesOrientation)
        }
    override val nextScramble: Scramble
        get() = Scramble(
                scramblerInfo.scramblerId,
                generate(randomState))

    override fun toString(): String {
        return scramblerInfo.description
    }

    init {
        random = Random()
    }
}