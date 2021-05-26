package com.puzzletimer.puzzles

import com.puzzletimer.solvers.RubiksClockSolver.State.rotateWheel
import com.puzzletimer.puzzles.Puzzle
import com.puzzletimer.models.PuzzleInfo
import com.puzzletimer.graphics.Plane
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.graphics.Mesh
import java.awt.Color
import com.puzzletimer.graphics.Matrix44
import com.puzzletimer.graphics.Vector3
import java.util.HashMap
import com.puzzletimer.graphics.Face
import com.puzzletimer.puzzles.RubiksPocketCube
import com.puzzletimer.puzzles.RubiksCube
import com.puzzletimer.puzzles.RubiksRevenge
import com.puzzletimer.puzzles.ProfessorsCube
import com.puzzletimer.puzzles.VCube6
import com.puzzletimer.puzzles.VCube7
import com.puzzletimer.puzzles.SS8
import com.puzzletimer.puzzles.SS9
import com.puzzletimer.puzzles.RubiksClock
import com.puzzletimer.puzzles.Megaminx
import com.puzzletimer.puzzles.Pyraminx
import com.puzzletimer.puzzles.Square1
import com.puzzletimer.puzzles.Skewb
import com.puzzletimer.puzzles.FloppyCube
import com.puzzletimer.puzzles.TowerCube
import com.puzzletimer.puzzles.RubiksTower
import com.puzzletimer.puzzles.RubiksDomino
import com.puzzletimer.solvers.RubiksClockSolver

class PuzzleProvider {
    val all: Array<Puzzle>
    private val puzzleMap: HashMap<String, Puzzle>
    operator fun get(puzzleId: String): Puzzle? {
        return puzzleMap[puzzleId]
    }

    init {
        all = arrayOf(
                RubiksPocketCube(),
                RubiksCube(),
                RubiksRevenge(),
                ProfessorsCube(),
                VCube6(),
                VCube7(),
                SS8(),
                SS9(),
                RubiksClock(),
                Megaminx(),
                Pyraminx(),
                Square1(),
                Skewb(),
                FloppyCube(),
                TowerCube(),
                RubiksTower(),
                RubiksDomino(),
                Other())
        puzzleMap = HashMap()
        for (puzzle in all) {
            puzzleMap[puzzle.puzzleInfo.puzzleId] = puzzle
        }
    }
}