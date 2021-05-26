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

class Skewb : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("SKEWB")

    override fun toString(): String {
        return puzzleInfo.description
    }

    private inner class Twist(var plane: Plane, var angle: Double)

    override fun getScrambledPuzzleMesh(colorScheme: ColorScheme, sequence: Array<String>): Mesh? {
        val colorArray = arrayOf(
                colorScheme.getFaceColor("FACE-L").color,
                colorScheme.getFaceColor("FACE-B").color,
                colorScheme.getFaceColor("FACE-D").color,
                colorScheme.getFaceColor("FACE-R").color,
                colorScheme.getFaceColor("FACE-F").color,
                colorScheme.getFaceColor("FACE-U").color)
        var mesh = Mesh.cube(colorArray)
        val planeL = Plane(
                Vector3(0.5, 0.5, 0),
                Vector3(0.5, 0, -0.5),
                Vector3(0, -0.5, -0.5))
        val planeR = Plane(
                Vector3(0.5, 0, -0.5),
                Vector3(0, 0.5, -0.5),
                Vector3(-0.5, 0.5, 0))
        val planeD = Plane(
                Vector3(0, 0.5, -0.5),
                Vector3(0.5, 0.5, 0),
                Vector3(0.5, 0, 0.5))
        val planeB = Plane(
                Vector3(0.5, 0, 0.5),
                Vector3(0, 0.5, 0.5),
                Vector3(-0.5, 0.5, 0))
        mesh = mesh
                .cut(planeL, 0.0)
                .cut(planeR, 0.0)
                .cut(planeD, 0.0)
                .cut(planeB, 0.0)
                .shortenFaces(0.05)
                .softenFaces(0.02)
                .softenFaces(0.01)
        val twists = HashMap<String, Twist>()
        twists["L"] = Twist(planeL, 2 * Math.PI / 3)
        twists["L'"] = Twist(planeL, -2 * Math.PI / 3)
        twists["R"] = Twist(planeR, 2 * Math.PI / 3)
        twists["R'"] = Twist(planeR, -2 * Math.PI / 3)
        twists["D"] = Twist(planeD, 2 * Math.PI / 3)
        twists["D'"] = Twist(planeD, -2 * Math.PI / 3)
        twists["B"] = Twist(planeB, 2 * Math.PI / 3)
        twists["B'"] = Twist(planeB, -2 * Math.PI / 3)
        for (move in sequence) {
            val t = twists[move]
            mesh = mesh.rotateHalfspace(t!!.plane, t.angle)
        }
        return mesh
                .transform(Matrix44.rotationY(-Math.PI / 6))
                .transform(Matrix44.rotationX(Math.PI / 7))
    }
}