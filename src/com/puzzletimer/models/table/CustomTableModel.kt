package com.puzzletimer.models.table

import javax.swing.table.DefaultTableModel
import javax.swing.JButton
import javax.swing.table.TableCellRenderer
import javax.swing.JTable
import com.puzzletimer.models.table.SortButtonRenderer
import java.awt.Insets
import javax.swing.SwingConstants
import com.puzzletimer.models.table.CustomTableModel

open class CustomTableModel : DefaultTableModel() {
    var indexes: IntArray?
    var sorter: TableSorter? = null
    override fun getValueAt(row: Int, col: Int): Any {
        var rowIndex = row
        if (indexes != null) {
            rowIndex = indexes!![row]
        }
        return super.getValueAt(rowIndex, col)
    }

    override fun setValueAt(value: Any, row: Int, col: Int) {
        var rowIndex = row
        if (indexes != null) {
            rowIndex = indexes!![row]
        }
        super.setValueAt(value, rowIndex, col)
    }

    fun sortByColumn(column: Int, isAscent: Boolean) {
        if (sorter == null) {
            sorter = TableSorter(this)
        }
        sorter!!.sort(column, isAscent)
        fireTableDataChanged()
    }

    fun getIndexes(): IntArray {
        val n = rowCount
        if (indexes != null) {
            if (indexes!!.size == n) {
                return indexes
            }
        }
        indexes = IntArray(n)
        for (i in 0 until n) {
            indexes!![i] = i
        }
        return indexes!!
    }
}