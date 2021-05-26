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

class Megaminx : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("MEGAMINX")

    override fun toString(): String {
        return puzzleInfo.description
    }

    private class Twist(var plane: Plane?, var angle: Double)

    override fun getScrambledPuzzleMesh(colorScheme: ColorScheme, sequence: Array<String>): Mesh? {
        val colorArray = arrayOf(
                colorScheme.getFaceColor("FACE-1").color,
                colorScheme.getFaceColor("FACE-2").color,
                colorScheme.getFaceColor("FACE-3").color,
                colorScheme.getFaceColor("FACE-4").color,
                colorScheme.getFaceColor("FACE-5").color,
                colorScheme.getFaceColor("FACE-6").color,
                colorScheme.getFaceColor("FACE-7").color,
                colorScheme.getFaceColor("FACE-8").color,
                colorScheme.getFaceColor("FACE-9").color,
                colorScheme.getFaceColor("FACE-10").color,
                colorScheme.getFaceColor("FACE-11").color,
                colorScheme.getFaceColor("FACE-12").color)
        var mesh = Mesh.dodecahedron(colorArray)
                .shortenFaces(0.025)
        val planes = arrayOfNulls<Plane>(mesh.faces.size)
        for (i in planes.indices) {
            val p = Plane(
                    mesh.faces[i].vertices[0],
                    mesh.faces[i].vertices[1],
                    mesh.faces[i].vertices[2])
            planes[i] = Plane(p.p.sub(p.n.mul(0.25)), p.n)
        }
        for (plane in planes) {
            mesh = mesh.cut(plane, 0.03)
        }
        mesh = mesh
                .softenFaces(0.01)
                .softenFaces(0.005)
        val planeR = Plane(planes[3]!!.p, planes[3]!!.n.neg())
        val planeD = Plane(planes[7]!!.p, planes[7]!!.n.neg())
        val planeU = planes[7]
        val twists = HashMap<String, Twist>()
        twists["R++"] = Twist(planeR, 4 * Math.PI / 5)
        twists["R--"] = Twist(planeR, -4 * Math.PI / 5)
        twists["D++"] = Twist(planeD, 4 * Math.PI / 5)
        twists["D--"] = Twist(planeD, -4 * Math.PI / 5)
        twists["U"] = Twist(planeU, 2 * Math.PI / 5)
        twists["U'"] = Twist(planeU, -2 * Math.PI / 5)
        for (move in sequence) {
            val t = twists[move]
            mesh = mesh.rotateHalfspace(t!!.plane, t.angle)
        }
        return mesh
                .transform(Matrix44.rotationY(Math.PI / 16))
    }
}