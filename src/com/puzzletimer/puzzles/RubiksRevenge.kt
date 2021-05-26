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

class RubiksRevenge : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("4x4x4-CUBE")

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
        val planeL = Plane(Vector3(-0.25, 0, 0), Vector3(-1, 0, 0))
        val planeLw = Plane(Vector3(-0.0, 0, 0), Vector3(-1, 0, 0))
        val planeR = Plane(Vector3(0.25, 0, 0), Vector3(1, 0, 0))
        val planeRw = Plane(Vector3(0.0, 0, 0), Vector3(1, 0, 0))
        val planeD = Plane(Vector3(0, -0.25, 0), Vector3(0, -1, 0))
        val planeDw = Plane(Vector3(0, -0.0, 0), Vector3(0, -1, 0))
        val planeU = Plane(Vector3(0, 0.25, 0), Vector3(0, 1, 0))
        val planeUw = Plane(Vector3(0, 0.0, 0), Vector3(0, 1, 0))
        val planeF = Plane(Vector3(0, 0, -0.25), Vector3(0, 0, -1))
        val planeFw = Plane(Vector3(0, 0, -0.0), Vector3(0, 0, -1))
        val planeB = Plane(Vector3(0, 0, 0.25), Vector3(0, 0, 1))
        val planeBw = Plane(Vector3(0, 0, 0.0), Vector3(0, 0, 1))
        mesh = mesh
                .cut(planeL, 0.0)
                .cut(planeR, 0.0)
                .cut(planeLw, 0.0)
                .cut(planeD, 0.0)
                .cut(planeU, 0.0)
                .cut(planeDw, 0.0)
                .cut(planeF, 0.0)
                .cut(planeB, 0.0)
                .cut(planeFw, 0.0)
                .shortenFaces(0.025)
                .softenFaces(0.015)
                .softenFaces(0.005)
        val twists = HashMap<String, Twist>()
        twists["L"] = Twist(planeL, Math.PI / 2)
        twists["Lw"] = Twist(planeLw, Math.PI / 2)
        twists["L2"] = Twist(planeL, Math.PI)
        twists["Lw2"] = Twist(planeLw, Math.PI)
        twists["L'"] = Twist(planeL, -Math.PI / 2)
        twists["Lw'"] = Twist(planeLw, -Math.PI / 2)
        twists["R"] = Twist(planeR, Math.PI / 2)
        twists["Rw"] = Twist(planeRw, Math.PI / 2)
        twists["R2"] = Twist(planeR, Math.PI)
        twists["Rw2"] = Twist(planeRw, Math.PI)
        twists["R'"] = Twist(planeR, -Math.PI / 2)
        twists["Rw'"] = Twist(planeRw, -Math.PI / 2)
        twists["D"] = Twist(planeD, Math.PI / 2)
        twists["Dw"] = Twist(planeDw, Math.PI / 2)
        twists["D2"] = Twist(planeD, Math.PI)
        twists["Dw2"] = Twist(planeDw, Math.PI)
        twists["D'"] = Twist(planeD, -Math.PI / 2)
        twists["Dw'"] = Twist(planeDw, -Math.PI / 2)
        twists["U"] = Twist(planeU, Math.PI / 2)
        twists["Uw"] = Twist(planeUw, Math.PI / 2)
        twists["U2"] = Twist(planeU, Math.PI)
        twists["Uw2"] = Twist(planeUw, Math.PI)
        twists["U'"] = Twist(planeU, -Math.PI / 2)
        twists["Uw'"] = Twist(planeUw, -Math.PI / 2)
        twists["F"] = Twist(planeF, Math.PI / 2)
        twists["Fw"] = Twist(planeFw, Math.PI / 2)
        twists["F2"] = Twist(planeF, Math.PI)
        twists["Fw2"] = Twist(planeFw, Math.PI)
        twists["F'"] = Twist(planeF, -Math.PI / 2)
        twists["Fw'"] = Twist(planeFw, -Math.PI / 2)
        twists["B"] = Twist(planeB, Math.PI / 2)
        twists["Bw"] = Twist(planeBw, Math.PI / 2)
        twists["B2"] = Twist(planeB, Math.PI)
        twists["Bw2"] = Twist(planeBw, Math.PI)
        twists["B'"] = Twist(planeB, -Math.PI / 2)
        twists["Bw'"] = Twist(planeBw, -Math.PI / 2)
        for (move in sequence) {
            val t = twists[move]
            mesh = mesh.rotateHalfspace(t!!.plane, t.angle)
        }
        return mesh
                .transform(Matrix44.rotationY(-Math.PI / 6))
                .transform(Matrix44.rotationX(Math.PI / 7))
    }
}