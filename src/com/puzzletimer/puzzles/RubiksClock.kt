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

class RubiksClock : Puzzle {
    override val puzzleInfo: PuzzleInfo
        get() = PuzzleInfo("RUBIKS-CLOCK")

    override fun toString(): String {
        return puzzleInfo.description
    }

    private fun stateFromSequence(sequence: Array<String>): RubiksClockSolver.State? {
        val pattern2 = Pattern.compile("([Ud]{4}) ([ud])=(-?\\d),([ud])=(-?\\d)")
        val pattern1 = Pattern.compile("([Ud]{4}) ([ud])=(-?\\d)")
        val pattern0 = Pattern.compile("([Ud]{4})")
        var state = RubiksClockSolver.State.id
        for (move in sequence) {
            // two wheels
            val matcher2 = pattern2.matcher(move)
            if (matcher2.find()) {
                val pins = matcher2.group(1)
                val pinsDown = BooleanArray(4)
                for (i in 0..3) {
                    pinsDown[i] = pins[i] != 'U'
                }
                var wheel1 = -1
                for (i in 0..3) {
                    if (matcher2.group(2)[0] == (if (pinsDown[i]) 'd' else 'u')) {
                        wheel1 = i
                    }
                }
                val turns1 = matcher2.group(3).toInt()
                state = state!!.rotateWheel(pinsDown, wheel1, turns1)
                var wheel2 = -1
                for (i in 0..3) {
                    if (matcher2.group(4)[0] == (if (pinsDown[i]) 'd' else 'u')) {
                        wheel2 = i
                    }
                }
                val turns2 = matcher2.group(5).toInt()
                state = state.rotateWheel(pinsDown, wheel2, turns2)
                continue
            }

            // one wheel
            val matcher1 = pattern1.matcher(move)
            if (matcher1.find()) {
                val pins = matcher1.group(1)
                val pinsDown = BooleanArray(4)
                for (i in 0..3) {
                    pinsDown[i] = pins[i] != 'U'
                }
                var wheel = -1
                for (i in 0..3) {
                    if (matcher1.group(2)[0] == (if (pinsDown[i]) 'd' else 'u')) {
                        wheel = i
                    }
                }
                val turns = matcher1.group(3).toInt()
                state = state!!.rotateWheel(pinsDown, wheel, turns)
                continue
            }

            // no rotation
            val matcher0 = pattern0.matcher(move)
            if (matcher0.find()) {
                val pins = matcher0.group(1)
                val pinsDown = BooleanArray(4)
                for (i in 0..3) {
                    pinsDown[i] = pins[i] != 'U'
                }
                state = state!!.rotateWheel(pinsDown, 0, 0)
            }
        }
        return state
    }

    private fun circle(radius: Double, nVertices: Int, color: Color): Mesh {
        val vertices = arrayOfNulls<Vector3>(nVertices)
        for (i in vertices.indices) {
            val x = radius * Math.cos(-2 * Math.PI * i / nVertices)
            val y = radius * Math.sin(-2 * Math.PI * i / nVertices)
            vertices[i] = Vector3(x, y, 0)
        }
        val faces = arrayOf(
                Face(vertices, color))
        return Mesh(faces)
    }

    private fun hand(radius1: Double, nVertices1: Int, radius2: Double, nVertices2: Int, height: Double, color: Color): Mesh {
        val vertices = arrayOfNulls<Vector3>(nVertices1 + nVertices2)
        var next = 0
        for (i in 0 until nVertices1) {
            val x = radius1 * Math.cos(-Math.PI * i / (nVertices1 - 1))
            val y = radius1 * Math.sin(-Math.PI * i / (nVertices1 - 1))
            vertices[next] = Vector3(x, y, 0)
            next++
        }
        for (i in 0 until nVertices2) {
            val x = radius2 * Math.cos(Math.PI - Math.PI * i / (nVertices2 - 1))
            val y = radius2 * Math.sin(Math.PI - Math.PI * i / (nVertices2 - 1))
            vertices[next] = Vector3(x, y + height, 0)
            next++
        }
        val faces = arrayOf(
                Face(vertices, color))
        return Mesh(faces)
    }

    override fun getScrambledPuzzleMesh(colorScheme: ColorScheme, sequence: Array<String>): Mesh? {
        val state = stateFromSequence(sequence)
        val handBackground = hand(0.04, 8, 0.001, 3, 0.16, colorScheme.getFaceColor("HAND-BACKGROUND").color).transform(
                Matrix44.translation(Vector3(0, 0, -0.025)))
        val handForeground = hand(0.025, 8, 0.001, 3, 0.11, colorScheme.getFaceColor("HAND-FOREGROUND").color).transform(
                Matrix44.translation(Vector3(0, 0, -0.05)))
        val hands = handBackground.union(handForeground)

        // front
        var front = Mesh(arrayOfNulls(0))
        for (i in 0..2) {
            for (j in 0..2) {
                val transformation = Matrix44.translation(Vector3(0.5 * (j - 1), 0.5 * (1 - i), 0)).mul(
                        Matrix44.rotationZ(Math.PI / 6 * state!!.clocks[3 * i + j]))
                front = front.union(
                        circle(0.225, 32, colorScheme.getFaceColor("FRONT").color).union(hands).transform(transformation))
            }
        }
        for (i in 0..1) {
            for (j in 0..1) {
                val pinDown = state!!.pinsDown!![2 * i + j]
                val transformation = Matrix44.translation(
                        Vector3(
                                0.5 * (j - 0.5),
                                0.5 * (0.5 - i),
                                if (pinDown) 0.0 else -0.1))
                val pinColor = colorScheme.getFaceColor(if (pinDown) "PIN-DOWN" else "PIN-UP").color
                front = front.union(
                        circle(0.05, 16, pinColor).transform(transformation))
            }
        }

        // back
        var back = Mesh(arrayOfNulls(0))
        for (i in 0..2) {
            for (j in 0..2) {
                val transformation = Matrix44.translation(Vector3(0.5 * (j - 1), 0.5 * (1 - i), 0)).mul(
                        Matrix44.rotationZ(Math.PI / 6 * state!!.clocks[9 + 3 * i + j]))
                back = back.union(
                        circle(0.225, 32, colorScheme.getFaceColor("BACK").color).union(hands).transform(transformation))
            }
        }
        for (i in 0..1) {
            for (j in 0..1) {
                val pinDown = !state!!.pinsDown!![2 * i + (1 - j)]
                val transformation = Matrix44.translation(
                        Vector3(
                                0.5 * (j - 0.5),
                                0.5 * (0.5 - i),
                                if (pinDown) 0.0 else -0.1))
                val pinColor = colorScheme.getFaceColor(if (pinDown) "PIN-DOWN" else "PIN-UP").color
                back = back.union(
                        circle(0.05, 16, pinColor).transform(transformation))
            }
        }
        back = back.transform(Matrix44.rotationY(Math.PI))
        return front.transform(Matrix44.translation(Vector3(0, 0, -0.1))).union(
                back.transform(Matrix44.translation(Vector3(0, 0, 0.1))))
    }
}