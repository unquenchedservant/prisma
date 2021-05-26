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

class Pyraminx : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("PYRAMINX")

    override fun toString(): String {
        return puzzleInfo.description
    }

    private class Twist(var plane: Plane, var angle: Double)

    override fun getScrambledPuzzleMesh(colorScheme: ColorScheme, sequence: Array<String>): Mesh? {
        val colorArray = arrayOf(
                colorScheme.getFaceColor("FACE-D").color,
                colorScheme.getFaceColor("FACE-L").color,
                colorScheme.getFaceColor("FACE-R").color,
                colorScheme.getFaceColor("FACE-F").color)
        var mesh = Mesh.tetrahedron(colorArray)
        val h1 = Math.sqrt(8.0) / 3.0 * (Math.sqrt(3.0) / 2.0 * 1.5)
        val plane1 = Plane(
                mesh.faces[0].vertices[0],
                mesh.faces[0].vertices[1],
                mesh.faces[0].vertices[2])
        val planeu = Plane(plane1.p.sub(plane1.n.mul(2.0 * h1 / 3.0)), plane1.n.neg())
        val planeU = Plane(plane1.p.sub(plane1.n.mul(h1 / 3.0)), plane1.n.neg())
        val plane2 = Plane(
                mesh.faces[1].vertices[0],
                mesh.faces[1].vertices[1],
                mesh.faces[1].vertices[2])
        val planer = Plane(plane2.p.sub(plane2.n.mul(2.0 * h1 / 3.0)), plane2.n.neg())
        val planeR = Plane(plane2.p.sub(plane2.n.mul(h1 / 3.0)), plane2.n.neg())
        val plane3 = Plane(
                mesh.faces[2].vertices[0],
                mesh.faces[2].vertices[1],
                mesh.faces[2].vertices[2])
        val planel = Plane(plane3.p.sub(plane3.n.mul(2.0 * h1 / 3.0)), plane3.n.neg())
        val planeL = Plane(plane3.p.sub(plane3.n.mul(h1 / 3.0)), plane3.n.neg())
        val plane4 = Plane(
                mesh.faces[3].vertices[0],
                mesh.faces[3].vertices[1],
                mesh.faces[3].vertices[2])
        val planeb = Plane(plane4.p.sub(plane4.n.mul(2.0 * h1 / 3.0)), plane4.n.neg())
        val planeB = Plane(plane4.p.sub(plane4.n.mul(h1 / 3.0)), plane4.n.neg())
        mesh = mesh
                .cut(planeu, 0.0)
                .cut(planeU, 0.0)
                .cut(planer, 0.0)
                .cut(planeR, 0.0)
                .cut(planel, 0.0)
                .cut(planeL, 0.0)
                .cut(planeb, 0.0)
                .cut(planeB, 0.0)
                .shortenFaces(0.05)
                .softenFaces(0.02)
                .softenFaces(0.01)
        val twists = HashMap<String, Twist>()
        twists["U"] = Twist(planeU, 2 * Math.PI / 3)
        twists["U'"] = Twist(planeU, -2 * Math.PI / 3)
        twists["u"] = Twist(planeu, 2 * Math.PI / 3)
        twists["u'"] = Twist(planeu, -2 * Math.PI / 3)
        twists["L"] = Twist(planeL, 2 * Math.PI / 3)
        twists["L'"] = Twist(planeL, -2 * Math.PI / 3)
        twists["l"] = Twist(planel, 2 * Math.PI / 3)
        twists["l'"] = Twist(planel, -2 * Math.PI / 3)
        twists["R"] = Twist(planeR, 2 * Math.PI / 3)
        twists["R'"] = Twist(planeR, -2 * Math.PI / 3)
        twists["r"] = Twist(planer, 2 * Math.PI / 3)
        twists["r'"] = Twist(planer, -2 * Math.PI / 3)
        twists["B"] = Twist(planeB, 2 * Math.PI / 3)
        twists["B'"] = Twist(planeB, -2 * Math.PI / 3)
        twists["b"] = Twist(planeb, 2 * Math.PI / 3)
        twists["b'"] = Twist(planeb, -2 * Math.PI / 3)
        for (move in sequence) {
            val t = twists[move]
            mesh = mesh.rotateHalfspace(t!!.plane, t.angle)
        }
        return mesh
                .transform(Matrix44.rotationY(-Math.PI / 4))
                .transform(Matrix44.rotationX(Math.PI / 6))
    }
}