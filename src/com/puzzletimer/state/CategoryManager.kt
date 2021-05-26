package com.puzzletimer.state

import com.puzzletimer.models.*
import com.puzzletimer.timer.Timer.stop
import com.puzzletimer.timer.Timer.setInspectionEnabled
import com.puzzletimer.timer.Timer.start
import com.puzzletimer.timer.Timer.setSmoothTimingEnabled
import java.util.Arrays
import java.util.HashMap
import com.puzzletimer.models.ColorScheme.FaceColor
import java.util.TimerTask
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.scramblers.Scrambler
import java.util.Collections
import java.util.UUID
import com.puzzletimer.state.UpdateManager
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import java.lang.StringBuilder
import kotlin.Throws
import java.io.IOException
import java.io.BufferedReader
import java.nio.charset.Charset
import java.util.ArrayList

class CategoryManager(categories: Array<Category?>, currentCategory: Category) {
    open class Listener {
        open fun categoryAdded(category: Category?) {}
        open fun categoryRemoved(category: Category?) {}
        open fun categoryUpdated(category: Category?) {}
        open fun currentCategoryChanged(category: Category?) {}
        open fun categoriesUpdated(categories: Array<Category>?, currentCategory: Category?) {}
    }

    private val listeners: ArrayList<Listener>
    private val categories: ArrayList<Category>
    private var currentCategory: Category
    fun getCategories(): Array<Category> {
        return categories.toTypedArray()
    }

    fun addCategory(category: Category) {
        categories.add(category)
        for (listener in listeners) {
            listener.categoryAdded(category)
        }
        notifyListeners()
    }

    fun removeCategory(category: Category) {
        categories.remove(category)
        for (listener in listeners) {
            listener.categoryRemoved(category)
        }
        notifyListeners()
    }

    fun updateCategory(category: Category) {
        for (i in categories.indices) {
            if (categories[i].categoryId == category.categoryId) {
                categories[i] = category
                break
            }
        }
        for (listener in listeners) {
            listener.categoryUpdated(category)
        }
        notifyListeners()
    }

    fun getCurrentCategory(): Category {
        return currentCategory
    }

    fun setCurrentCategory(category: Category) {
        currentCategory = category
        for (listener in listeners) {
            listener.currentCategoryChanged(currentCategory)
        }
        notifyListeners()
    }

    fun notifyListeners() {
        val categories = getCategories()
        for (listener in listeners) {
            listener.categoriesUpdated(categories, currentCategory)
        }
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    init {
        listeners = ArrayList()
        this.categories = ArrayList(Arrays.asList(*categories))
        this.currentCategory = currentCategory
    }
}