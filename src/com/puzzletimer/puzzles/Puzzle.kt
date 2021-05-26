package com.puzzletimer.puzzles

import com.puzzletimer.graphics.Mesh
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.models.PuzzleInfo

interface Puzzle {
    val puzzleInfo: PuzzleInfo
    fun getScrambledPuzzleMesh(colorScheme: ColorScheme, sequence: Array<String>): Mesh?
}