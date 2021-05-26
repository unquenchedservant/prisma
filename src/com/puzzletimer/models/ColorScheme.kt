package com.puzzletimer.models

import com.puzzletimer.util.StringUtils.join
import java.util.UUID
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.models.Scramble
import com.puzzletimer.models.Timing
import com.puzzletimer.models.Solution

class ColorScheme(val puzzleId: String, val faceColors: Array<FaceColor?>) {
    class FaceColor(val puzzleId: String, val faceId: String, val defaultColor: Color, val color: Color) {
        val faceDescription: String
            get() = _("face." + puzzleId + "." + faceId)

        fun setColorToDefault(): FaceColor {
            return FaceColor(
                    puzzleId,
                    faceId,
                    defaultColor,
                    defaultColor)
        }

        fun setColor(color: Color): FaceColor {
            return FaceColor(
                    puzzleId,
                    faceId,
                    defaultColor,
                    color)
        }
    }

    fun getFaceColor(faceId: String): FaceColor? {
        for (faceColor in faceColors) {
            if (faceColor!!.faceId == faceId) {
                return faceColor
            }
        }
        return null
    }

    fun setFaceColor(faceColor: FaceColor): ColorScheme {
        val faceColors = arrayOfNulls<FaceColor>(faceColors.size)
        for (i in faceColors.indices) {
            faceColors[i] = this.faceColors[i]
            if (this.faceColors[i]!!.faceId == faceColor.faceId) {
                faceColors[i] = faceColor
            }
        }
        return ColorScheme(puzzleId, faceColors)
    }
}