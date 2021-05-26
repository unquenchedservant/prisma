package com.puzzletimer.models.table

import javax.swing.table.DefaultTableModel
import javax.swing.JButton
import javax.swing.table.TableCellRenderer
import javax.swing.JTable
import com.puzzletimer.models.table.SortButtonRenderer
import java.awt.Insets
import javax.swing.SwingConstants
import com.puzzletimer.models.table.CustomTableModel
import java.awt.Component
import java.util.*

class SortButtonRenderer : JButton(), TableCellRenderer {
    var pushedColumn: Int
    var state: Hashtable<*, *>
    var downButton: JButton
    var upButton: JButton
    override fun getTableCellRendererComponent(table: JTable, value: Any,
                                               isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        var button: JButton = this
        val obj = state[column]
        if (obj != null) {
            button = if ((obj as Int).toInt() == DOWN) {
                downButton
            } else {
                upButton
            }
        }
        button.text = value?.toString() ?: ""
        val isPressed = column == pushedColumn
        button.model.isPressed = isPressed
        button.model.isArmed = isPressed
        return button
    }

    fun setPressedColumn(col: Int) {
        pushedColumn = col
    }

    fun setSelectedColumn(col: Int) {
        if (col < 0) return
        var value: Int? = null
        val obj = state[col]
        value = if (obj == null) {
            DOWN
        } else {
            if ((obj as Int).toInt() == DOWN) {
                UP
            } else {
                DOWN
            }
        }
        state.clear()
        state[col] = value
    }

    fun getState(col: Int): Int {
        val retValue: Int
        val obj = state[col]
        retValue = if (obj == null) {
            NONE
        } else {
            if ((obj as Int).toInt() == DOWN) {
                DOWN
            } else {
                UP
            }
        }
        return retValue
    }

    companion object {
        const val NONE = 0
        const val DOWN = 1
        const val UP = 2
    }

    init {
        pushedColumn = -1
        state = Hashtable<Any?, Any?>()
        margin = Insets(0, 0, 0, 0)
        horizontalTextPosition = LEFT
        //setIcon(new BlankIcon());

        // perplexed
        // ArrowIcon(SwingConstants.SOUTH, true)
        // BevelArrowIcon (int direction, boolean isRaisedView, boolean isPressedView)
        downButton = JButton()
        downButton.margin = Insets(0, 0, 0, 0)
        downButton.horizontalTextPosition = LEFT
        //downButton.setIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, false));
        //downButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, true));
        upButton = JButton()
        upButton.margin = Insets(0, 0, 0, 0)
        upButton.horizontalTextPosition = LEFT
        //upButton.setIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, false));
        //upButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, true));
    }
}