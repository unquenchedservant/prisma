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
import java.util.regex.Pattern

class Square1 : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("SQUARE-1")

    override fun toString(): String {
        return puzzleInfo.description
    }

    override fun getScrambledPuzzleMesh(colorScheme: ColorScheme, sequence: Array<String>): Mesh? {
        val colorArray = arrayOf(
                colorScheme.getFaceColor("FACE-L").color,
                colorScheme.getFaceColor("FACE-B").color,
                colorScheme.getFaceColor("FACE-D").color,
                colorScheme.getFaceColor("FACE-R").color,
                colorScheme.getFaceColor("FACE-F").color,
                colorScheme.getFaceColor("FACE-U").color)
        var cube = Mesh.cube(colorArray)
        val planeD = Plane(
                Vector3(0, -0.166, 0),
                Vector3(0, -1, 0))
        val planeU = Plane(
                Vector3(0, 0.166, 0),
                Vector3(0, 1, 0))
        val planeR = Plane(
                Vector3(0, 0, 0),
                Matrix44.rotationY(-Math.PI / 12).mul(Vector3(1, 0, 0)))
        val p1 = Plane(
                Vector3(0, 0, 0),
                Matrix44.rotationY(Math.PI / 12).mul(Vector3(1, 0, 0)))
        val p2 = Plane(
                Vector3(0, 0, 0),
                Matrix44.rotationY(-Math.PI / 12).mul(Vector3(0, 0, 1)))
        val p3 = Plane(
                Vector3(0, 0, 0),
                Matrix44.rotationY(Math.PI / 12).mul(Vector3(0, 0, 1)))
        val mesh = cube
                .cut(planeD, 0.01)
                .cut(planeU, 0.01)
                .cut(planeR, 0.01)
                .cut(p1, 0.01)
                .cut(p2, 0.01)
                .cut(p3, 0.01)
                .shortenFaces(0.02)
                .softenFaces(0.015)
                .softenFaces(0.005)
        val topLayer = mesh.clip(planeU)
        val bottomLayer = mesh.clip(planeD)
        val bandagedMesh = cube
                .cut(planeD, 0.01)
                .cut(planeU, 0.01)
                .cut(planeR, 0.01)
                .shortenFaces(0.02)
                .softenFaces(0.015)
                .softenFaces(0.005)
        val middleLayer = bandagedMesh
                .clip(Plane(planeU.p, planeU.n.neg()))
                .clip(Plane(planeD.p, planeD.n.neg()))
        cube = topLayer.union(middleLayer).union(bottomLayer)
        val p = Pattern.compile("\\((-?\\d+),(-?\\d+)\\)")
        for (m in sequence) {
            if (m == "/") {
                cube = cube.rotateHalfspace(planeR, Math.PI)
            } else {
                val matcher = p.matcher(m)
                matcher.find()
                val top = matcher.group(1).toInt()
                cube = cube.rotateHalfspace(planeU, top * Math.PI / 6)
                val bottom = matcher.group(2).toInt()
                cube = cube.rotateHalfspace(planeD, bottom * Math.PI / 6)
            }
        }
        return cube
                .transform(Matrix44.rotationY(-Math.PI / 6))
                .transform(Matrix44.rotationX(Math.PI / 7))
    }
}