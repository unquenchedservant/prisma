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
import java.util.ArrayList

class ScramblerProvider {
    private val scramblers: Array<Scrambler>
    private val scramblerMap: HashMap<String, Scrambler>
    val all: Array<Scrambler?>
        get() {
            val scramblers = ArrayList<Scrambler>()
            for (scrambler in this.scramblers) {
                if (!scrambler.scramblerInfo.scramblerId.endsWith("-IMPORTER")) {
                    scramblers.add(scrambler)
                }
            }
            val scramblersArray = arrayOfNulls<Scrambler>(scramblers.size)
            scramblers.toArray(scramblersArray)
            return scramblersArray
        }

    operator fun get(scramblerId: String): Scrambler? {
        return scramblerMap[scramblerId]
    }

    init {
        // 2x2x2 importer
        val rubiksPocketCubeImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("2x2x2-CUBE-IMPORTER", "2x2x2-CUBE", ""))

        // 2x2x2 random
        val rubiksPocketCubeRandom: Scrambler = RubiksPocketCubeRandomScrambler(
                ScramblerInfo("2x2x2-CUBE-RANDOM", "2x2x2-CUBE", _("scrambler.2x2x2-CUBE-RANDOM")),
                0, arrayOf("U", "D", "L", "R", "F", "B"))

        // 2x2x2 <U, R, F>
        val rubiksPocketCubeURF: Scrambler = RubiksPocketCubeRandomScrambler(
                ScramblerInfo("2x2x2-CUBE-URF", "2x2x2-CUBE", _("scrambler.2x2x2-CUBE-URF")),
                0, arrayOf("U", "R", "F"))

        // 2x2x2 suboptimal <U, R, F>
        val rubiksPocketCubeSuboptimalURF: Scrambler = RubiksPocketCubeRandomScrambler(
                ScramblerInfo("2x2x2-CUBE-SUBOPTIMAL-URF", "2x2x2-CUBE", _("scrambler.2x2x2-CUBE-SUBOPTIMAL-URF")),
                11, arrayOf("U", "R", "F"))

        // 3x3x3 importer
        val rubiksCubeImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("RUBIKS-CUBE-IMPORTER", "RUBIKS-CUBE", ""))

        // 3x3x3 random
        val rubiksCubeRandom: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-RANDOM", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-RANDOM")), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))

        // 3x3x3 <L, U>
        val rubiksCubeLU: Scrambler = RubiksCubeLUScrambler(
                ScramblerInfo("RUBIKS-CUBE-LU", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-LU")))

        // 3x3x3 <R, U>
        val rubiksCubeRU: Scrambler = RubiksCubeRUScrambler(
                ScramblerInfo("RUBIKS-CUBE-RU", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-RU")))

        // 3x3x3 CLL training
        val rubiksCubeCLLTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-CLL-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-CLL-TRAINING")), byteArrayOf(-1, -1, -1, -1, 4, 5, 6, 7), byteArrayOf(-1, -1, -1, -1, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 ELL training
        val rubiksCubeELLTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-ELL-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-ELL-TRAINING")), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, -1, -1, -1, -1, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0))

        // 3x3x3 fridrich f2l training
        val rubiksCubeFridrichF2LTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-FRIDRICH-F2L-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-FRIDRICH-F2L-TRAINING")), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, 8, 9, 10, 11), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0))

        // 3x3x3 fridrich oll training
        val rubiksCubeFridrichOLLTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-FRIDRICH-OLL-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-FRIDRICH-OLL-TRAINING")), byteArrayOf(-1, -1, -1, -1, 4, 5, 6, 7), byteArrayOf(-1, -1, -1, -1, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, -1, -1, -1, -1, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0))

        // 3x3x3 fridrich pll training
        val rubiksCubeFridrichPLLTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-FRIDRICH-PLL-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-FRIDRICH-PLL-TRAINING")), byteArrayOf(-1, -1, -1, -1, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, -1, -1, -1, -1, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 3op corners training
        val rubiksCube3OPCornersTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-CORNERS-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-CORNERS-TRAINING")), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 3op corners permutation training
        val rubiksCube3OPCornersPermutationTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-CORNERS-PERMUTATION-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-CORNERS-PERMUTATION-TRAINING")), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 3op corners orientation training
        val rubiksCube3OPCornersOrientationTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-CORNERS-ORIENTATION-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-CORNERS-ORIENTATION-TRAINING")), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 3op edges training
        val rubiksCube3OPEdgesTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-EDGES-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-EDGES-TRAINING")), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))

        // 3x3x3 3op edges permutation training
        val rubiksCube3OPEdgesPermutationTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-EDGES-PERMUTATION-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-EDGES-PERMUTATION-TRAINING")), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 3op edges orientation training
        val rubiksCube3OPEdgesOrientationTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-EDGES-ORIENTATION-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-EDGES-ORIENTATION-TRAINING")), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))

        // 3x3x3 3op orientation training
        val rubiksCube3OPOrientationTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-ORIENTATION-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-ORIENTATION-TRAINING")), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1))

        // 3x3x3 3op permutation training
        val rubiksCube3OPPermutationTraining: Scrambler = RubiksCubeRandomScrambler(
                ScramblerInfo("RUBIKS-CUBE-3OP-PERMUTATION-TRAINING", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-3OP-PERMUTATION-TRAINING")), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1), byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        // 3x3x3 bld single sticker cycle
        val rubiksCubeBLDSingleStickerCycle: Scrambler = RubiksCubeSingleStickerCycleScrambler(
                ScramblerInfo("RUBIKS-CUBE-BLD-SINGLE-STICKER-CYCLE", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-BLD-SINGLE-STICKER-CYCLE")))

        // 3x3x3 easy cross
        val rubiksCubeEasyCross: Scrambler = RubiksCubeEasyCrossScrambler(
                ScramblerInfo("RUBIKS-CUBE-EASY-CROSS", "RUBIKS-CUBE", _("scrambler.RUBIKS-CUBE-EASY-CROSS")),
                3)

        // 4x4x4 importer
        val rubiksRevengeImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("4x4x4-CUBE-IMPORTER", "4x4x4-CUBE", ""))

        // 4x4x4 random
        val rubiksRevengeRandom: Scrambler = RubiksRevengeRandomScrambler(
                ScramblerInfo("4x4x4-CUBE-RANDOM", "4x4x4-CUBE", _("scrambler.4x4x4-CUBE-RANDOM")),
                40)

        // 5x5x5 importer
        val professorsCubeImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("5x5x5-CUBE-IMPORTER", "5x5x5-CUBE", ""))

        // 5x5x5 random
        val professorsCubeRandom: Scrambler = ProfessorsCubeRandomScrambler(
                ScramblerInfo("5x5x5-CUBE-RANDOM", "5x5x5-CUBE", _("scrambler.5x5x5-CUBE-RANDOM")),
                60)

        // 6x6x6 importer
        val vCube6Importer: Scrambler = EmptyScrambler(
                ScramblerInfo("6x6x6-CUBE-IMPORTER", "6x6x6-CUBE", ""))

        // 6x6x6 random
        val vCube6Random: Scrambler = VCube6RandomScrambler(
                ScramblerInfo("6x6x6-CUBE-RANDOM", "6x6x6-CUBE", _("scrambler.6x6x6-CUBE-RANDOM")),
                80)

        // 7x7x7 importer
        val vCube7Importer: Scrambler = EmptyScrambler(
                ScramblerInfo("7x7x7-CUBE-IMPORTER", "7x7x7-CUBE", ""))

        // 7x7x7 random
        val vCube7Random: Scrambler = VCube7RandomScrambler(
                ScramblerInfo("7x7x7-CUBE-RANDOM", "7x7x7-CUBE", _("scrambler.7x7x7-CUBE-RANDOM")),
                100)

        // 8x8x8 importer
        val ss8Importer: Scrambler = EmptyScrambler(
                ScramblerInfo("8x8x8-CUBE-IMPORTER", "8x8x8-CUBE", ""))

        // 8x8x8 random
        val ss8Random: Scrambler = SS8RandomScrambler(
                ScramblerInfo("8x8x8-CUBE-RANDOM", "8x8x8-CUBE", _("scrambler.8x8x8-CUBE-RANDOM")),
                120)

        // 9x9x9 importer
        val ss9Importer: Scrambler = EmptyScrambler(
                ScramblerInfo("9x9x9-CUBE-IMPORTER", "9x9x9-CUBE", ""))

        // 9x9x9 random
        val ss9Random: Scrambler = SS9RandomScrambler(
                ScramblerInfo("9x9x9-CUBE-RANDOM", "9x9x9-CUBE", _("scrambler.9x9x9-CUBE-RANDOM")),
                140)

        // rubiks clock importer
        val rubiksClockImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("RUBIKS-CLOCK-IMPORTER", "RUBIKS-CLOCK", ""))

        // rubiks clock random
        val rubiksClockRandom: Scrambler = RubiksClockRandomScrambler(
                ScramblerInfo("RUBIKS-CLOCK-RANDOM", "RUBIKS-CLOCK", _("scrambler.RUBIKS-CLOCK-RANDOM")))

        // megaminx importer
        val megaminxImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("MEGAMINX-IMPORTER", "MEGAMINX", ""))

        // megaminx random
        val megaminxRandom: Scrambler = MegaminxRandomScrambler(
                ScramblerInfo("MEGAMINX-RANDOM", "MEGAMINX", _("scrambler.MEGAMINX-RANDOM")))

        // pyraminx importer
        val pyraminxImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("PYRAMINX-IMPORTER", "PYRAMINX", ""))

        // pyraminx random
        val pyraminxRandom: Scrambler = PyraminxRandomScrambler(
                ScramblerInfo("PYRAMINX-RANDOM", "PYRAMINX", _("scrambler.PYRAMINX-RANDOM")),
                0)

        // pyraminx random
        val pyraminxSuboptimalRandom: Scrambler = PyraminxRandomScrambler(
                ScramblerInfo("PYRAMINX-SUBOPTIMAL-RANDOM", "PYRAMINX", _("scrambler.PYRAMINX-SUBOPTIMAL-RANDOM")),
                11)

        // pyraminx importer
        val square1Importer: Scrambler = EmptyScrambler(
                ScramblerInfo("SQUARE-1-IMPORTER", "SQUARE-1", ""))

        // square-1 random
        val square1Random: Scrambler = Square1RandomScrambler(
                ScramblerInfo("SQUARE-1-RANDOM", "SQUARE-1", _("scrambler.SQUARE-1-RANDOM")))

        // square-1 cube shape
        val square1CubeShape: Scrambler = Square1CubeShapeScrambler(
                ScramblerInfo("SQUARE-1-CUBE-SHAPE", "SQUARE-1", _("scrambler.SQUARE-1-CUBE-SHAPE")))

        // skewb importer
        val skewbImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("SKEWB-IMPORTER", "SKEWB", ""))

        // skewb random
        val skewbRandom: Scrambler = SkewbRandomScrambler(
                ScramblerInfo("SKEWB-RANDOM", "SKEWB", _("scrambler.SKEWB-RANDOM")))

        // floppy cube importer
        val floppyCubeImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("FLOPPY-CUBE-IMPORTER", "FLOPPY-CUBE", ""))

        // floppy cube random
        val floppyCubeRandom: Scrambler = FloppyCubeRandomScrambler(
                ScramblerInfo("FLOPPY-CUBE-RANDOM", "FLOPPY-CUBE", _("scrambler.FLOPPY-CUBE-RANDOM")))

        // tower cube importer
        val towerCubeImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("TOWER-CUBE-IMPORTER", "TOWER-CUBE", ""))

        // tower cube random
        val towerCubeRandom: Scrambler = TowerCubeRandomScrambler(
                ScramblerInfo("TOWER-CUBE-RANDOM", "TOWER-CUBE", _("scrambler.TOWER-CUBE-RANDOM")))

        // rubiks tower importer
        val rubiksTowerImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("RUBIKS-TOWER-IMPORTER", "RUBIKS-TOWER", ""))

        // rubiks tower random
        val rubiksTowerRandom: Scrambler = RubiksTowerRandomScrambler(
                ScramblerInfo("RUBIKS-TOWER-RANDOM", "RUBIKS-TOWER", _("scrambler.RUBIKS-TOWER-RANDOM")))

        // rubik's domino importer
        val rubiksDominoImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("RUBIKS-DOMINO-IMPORTER", "RUBIKS-DOMINO", ""))

        // rubik's domino random
        val rubiksDominoRandom: Scrambler = RubiksDominoRandomScrambler(
                ScramblerInfo("RUBIKS-DOMINO-RANDOM", "RUBIKS-DOMINO", _("scrambler.RUBIKS-DOMINO-RANDOM")))

        // other importer
        val otherImporter: Scrambler = EmptyScrambler(
                ScramblerInfo("OTHER-IMPORTER", "OTHER", ""))

        // empty
        val empty: Scrambler = EmptyScrambler(
                ScramblerInfo("EMPTY", "OTHER", _("scrambler.EMPTY")))
        scramblers = arrayOf(
                rubiksPocketCubeImporter,
                rubiksPocketCubeRandom,
                rubiksPocketCubeURF,
                rubiksPocketCubeSuboptimalURF,
                rubiksCubeImporter,
                rubiksCubeRandom,
                rubiksCubeLU,
                rubiksCubeRU,
                rubiksCubeCLLTraining,
                rubiksCubeELLTraining,
                rubiksCubeFridrichF2LTraining,
                rubiksCubeFridrichOLLTraining,
                rubiksCubeFridrichPLLTraining,
                rubiksCube3OPCornersTraining,
                rubiksCube3OPCornersPermutationTraining,
                rubiksCube3OPCornersOrientationTraining,
                rubiksCube3OPEdgesTraining,
                rubiksCube3OPEdgesPermutationTraining,
                rubiksCube3OPEdgesOrientationTraining,
                rubiksCube3OPOrientationTraining,
                rubiksCube3OPPermutationTraining,
                rubiksCubeBLDSingleStickerCycle,
                rubiksCubeEasyCross,
                rubiksRevengeImporter,
                rubiksRevengeRandom,
                professorsCubeImporter,
                professorsCubeRandom,
                vCube6Importer,
                vCube6Random,
                vCube7Importer,
                vCube7Random,
                ss8Importer,
                ss8Random,
                ss9Importer,
                ss9Random,
                rubiksClockImporter,
                rubiksClockRandom,
                megaminxImporter,
                megaminxRandom,
                pyraminxImporter,
                pyraminxRandom,
                pyraminxSuboptimalRandom,
                skewbImporter,
                skewbRandom,
                square1Importer,
                square1Random,
                square1CubeShape,
                floppyCubeImporter,
                floppyCubeRandom,
                towerCubeImporter,
                towerCubeRandom,
                rubiksTowerImporter,
                rubiksTowerRandom,
                rubiksDominoImporter,
                rubiksDominoRandom,
                otherImporter,
                empty)
        scramblerMap = HashMap()
        for (scrambler in scramblers) {
            scramblerMap[scrambler.scramblerInfo.scramblerId] = scrambler
        }
    }
}