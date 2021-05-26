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

class SS9 : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("9x9x9-CUBE")

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
        val planeL = Plane(Vector3(-0.3888888889, 0, 0), Vector3(-1, 0, 0))
        val planeL2 = Plane(Vector3(-0.2777777778, 0, 0), Vector3(-1, 0, 0))
        val planeL3 = Plane(Vector3(-0.1666666667, 0, 0), Vector3(-1, 0, 0))
        val planeL4 = Plane(Vector3(-0.0555555556, 0, 0), Vector3(-1, 0, 0))
        val planeR4 = Plane(Vector3(0.0555555556, 0, 0), Vector3(1, 0, 0))
        val planeR3 = Plane(Vector3(0.1666666667, 0, 0), Vector3(1, 0, 0))
        val planeR2 = Plane(Vector3(0.2777777778, 0, 0), Vector3(1, 0, 0))
        val planeR = Plane(Vector3(0.3888888889, 0, 0), Vector3(1, 0, 0))
        val planeD = Plane(Vector3(0, -0.3888888889, 0), Vector3(0, -1, 0))
        val planeD2 = Plane(Vector3(0, -0.2777777778, 0), Vector3(0, -1, 0))
        val planeD3 = Plane(Vector3(0, -0.1666666667, 0), Vector3(0, -1, 0))
        val planeD4 = Plane(Vector3(0, -0.0555555556, 0), Vector3(0, -1, 0))
        val planeU4 = Plane(Vector3(0, 0.0555555556, 0), Vector3(0, 1, 0))
        val planeU3 = Plane(Vector3(0, 0.1666666667, 0), Vector3(0, 1, 0))
        val planeU2 = Plane(Vector3(0, 0.2777777778, 0), Vector3(0, 1, 0))
        val planeU = Plane(Vector3(0, 0.3888888889, 0), Vector3(0, 1, 0))
        val planeF = Plane(Vector3(0, 0, -0.3888888889), Vector3(0, 0, -1))
        val planeF2 = Plane(Vector3(0, 0, -0.2777777778), Vector3(0, 0, -1))
        val planeF3 = Plane(Vector3(0, 0, -0.1666666667), Vector3(0, 0, -1))
        val planeF4 = Plane(Vector3(0, 0, -0.0555555556), Vector3(0, 0, -1))
        val planeB4 = Plane(Vector3(0, 0, 0.0555555556), Vector3(0, 0, 1))
        val planeB3 = Plane(Vector3(0, 0, 0.1666666667), Vector3(0, 0, 1))
        val planeB2 = Plane(Vector3(0, 0, 0.2777777778), Vector3(0, 0, 1))
        val planeB = Plane(Vector3(0, 0, 0.3888888889), Vector3(0, 0, 1))
        mesh = mesh
                .cut(planeL, 0.0)
                .cut(planeL2, 0.0)
                .cut(planeL3, 0.0)
                .cut(planeL4, 0.0)
                .cut(planeR4, 0.0)
                .cut(planeR3, 0.0)
                .cut(planeR2, 0.0)
                .cut(planeR, 0.0)
                .cut(planeD, 0.0)
                .cut(planeD2, 0.0)
                .cut(planeD3, 0.0)
                .cut(planeD4, 0.0)
                .cut(planeU4, 0.0)
                .cut(planeU3, 0.0)
                .cut(planeU2, 0.0)
                .cut(planeU, 0.0)
                .cut(planeF, 0.0)
                .cut(planeF2, 0.0)
                .cut(planeF3, 0.0)
                .cut(planeF4, 0.0)
                .cut(planeB4, 0.0)
                .cut(planeB3, 0.0)
                .cut(planeB2, 0.0)
                .cut(planeB, 0.0)
                .shortenFaces(0.015)
                .softenFaces(0.01)
                .softenFaces(0.005)
        val twists = HashMap<String, Twist>()
        twists["L"] = Twist(planeL, Math.PI / 2)
        twists["2L"] = Twist(planeL2, Math.PI / 2)
        twists["3L"] = Twist(planeL3, Math.PI / 2)
        twists["4L"] = Twist(planeL4, Math.PI / 2)
        twists["L2"] = Twist(planeL, Math.PI)
        twists["2L2"] = Twist(planeL2, Math.PI)
        twists["3L2"] = Twist(planeL3, Math.PI)
        twists["4L2"] = Twist(planeL4, Math.PI)
        twists["L'"] = Twist(planeL, -Math.PI / 2)
        twists["2L'"] = Twist(planeL2, -Math.PI / 2)
        twists["3L'"] = Twist(planeL3, -Math.PI / 2)
        twists["4L'"] = Twist(planeL4, -Math.PI / 2)
        twists["R"] = Twist(planeR, Math.PI / 2)
        twists["2R"] = Twist(planeR2, Math.PI / 2)
        twists["3R"] = Twist(planeR3, Math.PI / 2)
        twists["4R"] = Twist(planeR4, Math.PI / 2)
        twists["R2"] = Twist(planeR, Math.PI)
        twists["2R2"] = Twist(planeR2, Math.PI)
        twists["3R2"] = Twist(planeR3, Math.PI)
        twists["4R2"] = Twist(planeR4, Math.PI)
        twists["R'"] = Twist(planeR, -Math.PI / 2)
        twists["2R'"] = Twist(planeR2, -Math.PI / 2)
        twists["3R'"] = Twist(planeR3, -Math.PI / 2)
        twists["4R'"] = Twist(planeR4, -Math.PI / 2)
        twists["D"] = Twist(planeD, Math.PI / 2)
        twists["2D"] = Twist(planeD2, Math.PI / 2)
        twists["3D"] = Twist(planeD3, Math.PI / 2)
        twists["4D"] = Twist(planeD4, Math.PI / 2)
        twists["D2"] = Twist(planeD, Math.PI)
        twists["2D2"] = Twist(planeD2, Math.PI)
        twists["3D2"] = Twist(planeD3, Math.PI)
        twists["4D2"] = Twist(planeD4, Math.PI)
        twists["D'"] = Twist(planeD, -Math.PI / 2)
        twists["2D'"] = Twist(planeD2, -Math.PI / 2)
        twists["3D'"] = Twist(planeD3, -Math.PI / 2)
        twists["4D'"] = Twist(planeD4, -Math.PI / 2)
        twists["U"] = Twist(planeU, Math.PI / 2)
        twists["2U"] = Twist(planeU2, Math.PI / 2)
        twists["3U"] = Twist(planeU3, Math.PI / 2)
        twists["4U"] = Twist(planeU4, Math.PI / 2)
        twists["U2"] = Twist(planeU, Math.PI)
        twists["2U2"] = Twist(planeU2, Math.PI)
        twists["3U2"] = Twist(planeU3, Math.PI)
        twists["4U2"] = Twist(planeU4, Math.PI)
        twists["U'"] = Twist(planeU, -Math.PI / 2)
        twists["2U'"] = Twist(planeU2, -Math.PI / 2)
        twists["3U'"] = Twist(planeU3, -Math.PI / 2)
        twists["4U'"] = Twist(planeU4, -Math.PI / 2)
        twists["F"] = Twist(planeF, Math.PI / 2)
        twists["2F"] = Twist(planeF2, Math.PI / 2)
        twists["3F"] = Twist(planeF3, Math.PI / 2)
        twists["4F"] = Twist(planeF4, Math.PI / 2)
        twists["F2"] = Twist(planeF, Math.PI)
        twists["2F2"] = Twist(planeF2, Math.PI)
        twists["3F2"] = Twist(planeF3, Math.PI)
        twists["4F2"] = Twist(planeF4, Math.PI)
        twists["F'"] = Twist(planeF, -Math.PI / 2)
        twists["2F'"] = Twist(planeF2, -Math.PI / 2)
        twists["3F'"] = Twist(planeF3, -Math.PI / 2)
        twists["4F'"] = Twist(planeF4, -Math.PI / 2)
        twists["B"] = Twist(planeB, Math.PI / 2)
        twists["2B"] = Twist(planeB2, Math.PI / 2)
        twists["3B"] = Twist(planeB3, Math.PI / 2)
        twists["4B"] = Twist(planeB4, Math.PI / 2)
        twists["B2"] = Twist(planeB, Math.PI)
        twists["2B2"] = Twist(planeB2, Math.PI)
        twists["3B2"] = Twist(planeB3, Math.PI)
        twists["4B2"] = Twist(planeB4, Math.PI)
        twists["B'"] = Twist(planeB, -Math.PI / 2)
        twists["2B'"] = Twist(planeB2, -Math.PI / 2)
        twists["3B'"] = Twist(planeB3, -Math.PI / 2)
        twists["4B'"] = Twist(planeB4, -Math.PI / 2)
        for (move in sequence) {
            val t = twists[move]
            mesh = mesh.rotateHalfspace(t!!.plane, t.angle)
        }
        return mesh
                .transform(Matrix44.rotationY(-Math.PI / 6))
                .transform(Matrix44.rotationX(Math.PI / 7))
    }
}