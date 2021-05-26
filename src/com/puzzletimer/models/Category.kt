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

class Category(private val categoryId: UUID?, private val scramblerId: String?, private val description: String?, val isUserDefined: Boolean, val tipIds: Array<String?>) {
    fun getCategoryId(): UUID? {
        return categoryId ?: UUID.fromString("None")
    }

    fun getScramblerId(): String? {
        return scramblerId ?: ""
    }

    fun setScramblerId(scramblerId: String?): Category {
        return Category(
                categoryId,
                scramblerId,
                description,
                isUserDefined,
                tipIds)
    }

    fun getDescription(): String? {
        // descriptions of built-in categories
        val descriptions = HashMap<UUID?, String>()
        descriptions[UUID.fromString("64b9c16d-dc36-44b4-9605-c93933cdd311")] = keyify("category.2x2x2_cube")
        descriptions[UUID.fromString("90dea358-e525-4b6c-8b2d-abfa61f02a9d")] = keyify("category.rubiks_cube")
        descriptions[UUID.fromString("3282c6bc-3a7b-4b16-aeae-45ae75b17e47")] = keyify("category.rubiks_cube_one_handed")
        descriptions[UUID.fromString("953a7701-6235-4f9b-8dd4-fe32055cb652")] = keyify("category.rubiks_cube_blindfolded")
        descriptions[UUID.fromString("761088a1-64fc-47db-92ea-b6c3b812e6f3")] = keyify("category.rubiks_cube_with_feet")
        descriptions[UUID.fromString("3577f24a-065b-4bcc-9ca3-3df011d07a5d")] = keyify("category.4x4x4_cube")
        descriptions[UUID.fromString("587d884a-b996-4cd6-95bb-c3dafbfae193")] = keyify("category.4x4x4_cube_blindfolded")
        descriptions[UUID.fromString("e3894e40-fb85-497b-a592-c81703901a95")] = keyify("category.5x5x5_cube")
        descriptions[UUID.fromString("0701c98c-a275-4e51-888c-59dc9de9de1a")] = keyify("category.5x5x5_cube_blindfolded")
        descriptions[UUID.fromString("86227762-6249-4417-840b-3c8ba7b0bd33")] = keyify("category.6x6x6_cube")
        descriptions[UUID.fromString("b9375ece-5a31-4dc4-b58e-ecb8a638e102")] = keyify("category.7x7x7_cube")
        descriptions[UUID.fromString("08831818-6d8c-41fb-859e-a29b507f49fa")] = keyify("category.8x8x8_cube")
        descriptions[UUID.fromString("2fe5cacf-55df-4f5c-b811-f64c54959c44")] = keyify("category.9x9x9_cube")
        descriptions[UUID.fromString("7f244648-0e14-44cd-8399-b41ccdb6d7db")] = keyify("category.rubiks_clock")
        descriptions[UUID.fromString("c50f60c8-99d2-48f4-8502-d110a0ef2fc9")] = keyify("category.megaminx")
        descriptions[UUID.fromString("6750cbfd-542d-42b7-9cf4-56265549dd88")] = keyify("category.pyraminx")
        descriptions[UUID.fromString("748e6c09-cca5-412a-bd92-cc7febed9adf")] = keyify("category.square_1")
        descriptions[UUID.fromString("1a647910-41ff-48d1-b9f5-6f1874da9265")] = keyify("category.rubiks_magic")
        descriptions[UUID.fromString("f8f96514-bcb8-4f46-abb5-aecb7da4e4de")] = keyify("category.master_magic")
        return if (descriptions.containsKey(categoryId)) {
            descriptions[categoryId]
        } else description
    }

    fun setDescription(description: String?): Category {
        return Category(
                categoryId,
                scramblerId,
                description,
                isUserDefined,
                tipIds)
    }

    fun setTipIds(tipIds: Array<String?>): Category {
        return Category(
                categoryId,
                scramblerId,
                description,
                isUserDefined,
                tipIds)
    }
}