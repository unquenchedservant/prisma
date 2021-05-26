package com.puzzletimer

import java.util.ResourceBundle
import com.puzzletimer.Internationalization

object Internationalization {
    private val resourceBundle: ResourceBundle? = null
    fun keyify(key: String): String {
        return if (resourceBundle!!.containsKey(key)) {
            resourceBundle.getString(key)
        } else "*** $key ***"
    }

    init {
        resourceBundle = ResourceBundle.getBundle("com.puzzletimer.resources.i18n.messages")
    }
}