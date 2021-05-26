package com.puzzletimer.models.table

import javax.swing.table.DefaultTableModel
import javax.swing.JButton
import javax.swing.table.TableCellRenderer
import javax.swing.JTable
import com.puzzletimer.models.table.SortButtonRenderer
import java.awt.Insets
import javax.swing.SwingConstants
import com.puzzletimer.models.table.CustomTableModel
import java.util.*

class TableSorter(var model: CustomTableModel) {
    //n2 selection
    fun sort(column: Int, isAscent: Boolean) {
        val n = model.rowCount
        val indexes = model.getIndexes()
        for (i in 0 until n - 1) {
            var k = i
            for (j in i + 1 until n) {
                if (isAscent) {
                    if (compare(column, j, k) < 0) {
                        k = j
                    }
                } else {
                    if (compare(column, j, k) > 0) {
                        k = j
                    }
                }
            }
            val tmp = indexes!![i]
            indexes[i] = indexes[k]
            indexes[k] = tmp
        }
    }

    // comparaters
    fun compare(column: Int, row1: Int, row2: Int): Int {
        val o1 = model.getValueAt(row1, column)
        val o2 = model.getValueAt(row2, column)
        return if (o1 == null && o2 == null) {
            0
        } else if (o1 == null) {
            -1
        } else if (o2 == null) {
            1
        } else {
            val type = model.getColumnClass(column)
            if (model.getColumnName(column) == _("history.comment") || model.getColumnName(column) == _("history.scramble")) {
                0
            } else if (type.superclass == Number::class.java) {
                compare(o1 as Number, o2 as Number)
            } else if (type == String::class.java) {
                (o1 as String).compareTo((o2 as String))
            } else if (type == Date::class.java) {
                compare(o1 as Date, o2 as Date)
            } else if (type == Boolean::class.java) {
                compare(o1 as Boolean, o2 as Boolean)
            } else if (model.getColumnName(column) == _("history.#")) {
                compare(o1 as Int, o2 as Int)
            } else {
                (o1 as String).compareTo((o2 as String))
            }
        }
    }

    fun compare(o1: Number, o2: Number): Int {
        val n1 = o1.toDouble()
        val n2 = o2.toDouble()
        return if (n1 < n2) {
            -1
        } else if (n1 > n2) {
            1
        } else {
            0
        }
    }

    fun compare(o1: Date, o2: Date): Int {
        val n1 = o1.time
        val n2 = o2.time
        return if (n1 < n2) {
            -1
        } else if (n1 > n2) {
            1
        } else {
            0
        }
    }

    fun compare(o1: Boolean, o2: Boolean): Int {
        val b1: Boolean = o1.toBoolean()
        val b2: Boolean = o2.toBoolean()
        return if (b1 == b2) {
            0
        } else if (b1) {
            1
        } else {
            -1
        }
    }
}