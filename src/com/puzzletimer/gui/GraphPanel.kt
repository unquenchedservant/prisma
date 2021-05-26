package com.puzzletimer.gui

import com.puzzletimer.puzzles.Puzzle.getScrambledPuzzleMesh
import com.puzzletimer.models.Category.getDescription
import com.puzzletimer.puzzles.Puzzle.puzzleInfo
import com.puzzletimer.scramblers.Scrambler.scramblerInfo
import com.puzzletimer.models.ScramblerInfo.puzzleId
import com.puzzletimer.models.PuzzleInfo.puzzleId
import com.puzzletimer.tips.Tip.puzzleId
import com.puzzletimer.models.Category.tipIds
import com.puzzletimer.tips.Tip.tipId
import com.puzzletimer.models.ScramblerInfo.scramblerId
import com.puzzletimer.models.Category.setScramblerId
import com.puzzletimer.models.Category.setDescription
import com.puzzletimer.models.Category.setTipIds
import com.puzzletimer.models.Category.getScramblerId
import com.puzzletimer.state.CategoryManager.addListener
import com.puzzletimer.scramblers.ScramblerProvider.get
import com.puzzletimer.puzzles.PuzzleProvider.get
import com.puzzletimer.models.PuzzleInfo.description
import com.puzzletimer.models.ScramblerInfo.description
import com.puzzletimer.state.CategoryManager.getCategories
import com.puzzletimer.state.CategoryManager.getCurrentCategory
import com.puzzletimer.models.Category.isUserDefined
import com.puzzletimer.state.CategoryManager.addCategory
import com.puzzletimer.puzzles.PuzzleProvider.all
import com.puzzletimer.scramblers.ScramblerProvider.all
import com.puzzletimer.tips.TipProvider.all
import com.puzzletimer.state.CategoryManager.updateCategory
import com.puzzletimer.state.CategoryManager.removeCategory
import com.puzzletimer.state.ColorManager.getColorScheme
import com.puzzletimer.models.ColorScheme.faceColors
import com.puzzletimer.models.ColorScheme.FaceColor.faceDescription
import com.puzzletimer.models.ColorScheme.FaceColor.color
import com.puzzletimer.state.ColorManager.setColorScheme
import com.puzzletimer.models.ColorScheme.setFaceColor
import com.puzzletimer.models.ColorScheme.FaceColor.setColor
import com.puzzletimer.models.ColorScheme.FaceColor.setColorToDefault
import com.puzzletimer.state.ColorManager.addListener
import com.puzzletimer.models.ColorScheme.puzzleId
import com.puzzletimer.util.SolutionUtils.realTime
import com.puzzletimer.state.ConfigurationManager.getConfiguration
import com.puzzletimer.models.Solution.timing
import com.puzzletimer.models.Timing.start
import com.puzzletimer.util.SolutionUtils.format
import com.puzzletimer.util.SolutionUtils.realTimes
import com.puzzletimer.util.SolutionUtils.parseTime
import com.puzzletimer.parsers.ScrambleParser.parse
import com.puzzletimer.state.CategoryManager.notifyListeners
import com.puzzletimer.state.TimerManager.addListener
import com.puzzletimer.state.SolutionManager.getSolutions
import com.puzzletimer.state.SolutionManager.addListener
import com.puzzletimer.state.SolutionManager.notifyListeners
import com.puzzletimer.state.SolutionManager.addSolutions
import com.puzzletimer.parsers.ScrambleParserProvider.get
import com.puzzletimer.models.Category.getCategoryId
import com.puzzletimer.state.SolutionManager.updateSolution
import com.puzzletimer.state.SolutionManager.removeSolution
import com.puzzletimer.state.SessionManager.getSolutions
import com.puzzletimer.models.Solution.solutionId
import com.puzzletimer.statistics.StatisticalMeasure.minimumWindowSize
import com.puzzletimer.statistics.StatisticalMeasure.maximumWindowSize
import com.puzzletimer.statistics.StatisticalMeasure.setSolutions
import com.puzzletimer.util.SolutionUtils.formatMinutes
import com.puzzletimer.statistics.StatisticalMeasure.value
import com.puzzletimer.statistics.StatisticalMeasure.round
import com.puzzletimer.statistics.StatisticalMeasure.windowPosition
import com.puzzletimer.models.Timing.elapsedTime
import com.puzzletimer.models.Solution.penalty
import com.puzzletimer.models.Solution.comment
import com.puzzletimer.models.Solution.scramble
import com.puzzletimer.models.Scramble.rawSequence
import com.puzzletimer.models.table.SortButtonRenderer.setPressedColumn
import com.puzzletimer.models.table.SortButtonRenderer.setSelectedColumn
import com.puzzletimer.models.table.SortButtonRenderer.getState
import com.puzzletimer.models.table.CustomTableModel.sortByColumn
import com.puzzletimer.state.ScrambleManager.addListener
import com.puzzletimer.models.Scramble.sequence
import com.puzzletimer.models.Scramble.scramblerId
import com.puzzletimer.state.SessionManager.addListener
import com.puzzletimer.models.Solution.setPenalty
import com.puzzletimer.state.ScrambleManager.addScrambles
import com.puzzletimer.state.ScrambleManager.changeScramble
import com.puzzletimer.state.MessageManager.enqueueMessage
import com.puzzletimer.state.UpdateManager.checkUpdate
import com.puzzletimer.state.UpdateManager.latest
import com.puzzletimer.state.UpdateManager.getVersionNumber
import com.puzzletimer.state.UpdateManager.getDescription
import com.puzzletimer.state.ScrambleManager.currentScramble
import com.puzzletimer.state.SolutionManager.addSolution
import com.puzzletimer.state.CategoryManager.setCurrentCategory
import com.puzzletimer.state.TimerManager.isInspectionEnabled
import com.puzzletimer.state.TimerManager.setInspectionEnabled
import com.puzzletimer.state.TimerManager.isAnyKeyEnabled
import com.puzzletimer.state.TimerManager.setAnyKeyEnabled
import com.puzzletimer.state.SessionManager.isDailySessionEnabled
import com.puzzletimer.state.TimerManager.isHideTimerEnabled
import com.puzzletimer.state.TimerManager.setHideTimerEnabled
import com.puzzletimer.state.TimerManager.isSmoothTimingEnabled
import com.puzzletimer.state.TimerManager.setSmoothTimingEnabled
import com.puzzletimer.state.ConfigurationManager.setConfiguration
import com.puzzletimer.util.ExportUtils.ExportToFile
import com.puzzletimer.state.MessageManager.addListener
import com.puzzletimer.state.TimerManager.setTimer
import com.puzzletimer.state.TimerManager.setPrecision
import com.puzzletimer.state.ScrambleManager.moveScramblesUp
import com.puzzletimer.state.ScrambleManager.moveScramblesDown
import com.puzzletimer.state.ScrambleManager.removeScrambles
import com.puzzletimer.state.ScrambleManager.getQueue
import com.puzzletimer.scramblers.Scrambler.nextScramble
import com.puzzletimer.models.Timing.end
import com.puzzletimer.models.Solution.setTiming
import com.puzzletimer.models.Solution.setComment
import com.puzzletimer.tips.TipProvider.get
import com.puzzletimer.tips.Tip.getTip
import javax.swing.JFrame
import javax.swing.JDialog
import com.puzzletimer.puzzles.Puzzle
import com.puzzletimer.puzzles.RubiksCube
import com.puzzletimer.models.ColorScheme
import com.puzzletimer.models.ColorScheme.FaceColor
import java.awt.Color
import com.puzzletimer.graphics.Panel3D
import com.puzzletimer.graphics.Vector3
import javax.swing.JLabel
import java.awt.Font
import com.puzzletimer.scramblers.Scrambler
import com.puzzletimer.tips.Tip
import com.puzzletimer.gui.CategoryEditorListener
import javax.swing.JTextField
import javax.swing.JComboBox
import javax.swing.JButton
import javax.swing.JList
import javax.swing.DefaultListModel
import javax.swing.ListSelectionModel
import javax.swing.JScrollPane
import java.awt.Dimension
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.puzzletimer.models.PuzzleInfo
import com.puzzletimer.models.ScramblerInfo
import javax.swing.ListModel
import javax.swing.KeyStroke
import java.awt.event.KeyEvent
import javax.swing.JComponent
import com.puzzletimer.puzzles.PuzzleProvider
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.state.CategoryManager
import com.puzzletimer.tips.TipProvider
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import com.puzzletimer.gui.CategoryEditorDialog
import javax.swing.JOptionPane
import com.puzzletimer.state.ColorManager
import javax.swing.table.TableCellRenderer
import javax.swing.BorderFactory
import com.puzzletimer.gui.ColorSchemeFrame.ColorRenderer
import javax.swing.JColorChooser
import javax.swing.WindowConstants
import com.puzzletimer.models.Solution
import com.puzzletimer.state.ConfigurationManager
import javax.swing.JPanel
import com.puzzletimer.util.SolutionUtils
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.FontMetrics
import java.text.DateFormat
import java.awt.geom.GeneralPath
import java.awt.geom.AffineTransform
import com.puzzletimer.parsers.ScrambleParserProvider
import com.puzzletimer.state.ScrambleManager
import com.puzzletimer.state.SolutionManager
import com.puzzletimer.state.TimerManager
import com.puzzletimer.parsers.ScrambleParser
import com.puzzletimer.gui.HistoryFrame.SolutionImporterDialog.SolutionImporterListener
import javax.swing.JTextArea
import com.puzzletimer.models.Timing
import com.puzzletimer.models.Scramble
import com.puzzletimer.gui.HistogramPanel
import com.puzzletimer.gui.GraphPanel
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.statistics.Best
import com.puzzletimer.statistics.Mean
import com.puzzletimer.statistics.BestMean
import com.puzzletimer.statistics.Percentile
import com.puzzletimer.statistics.Average
import com.puzzletimer.statistics.InterquartileMean
import com.puzzletimer.statistics.BestAverage
import com.puzzletimer.statistics.StandardDeviation
import com.puzzletimer.statistics.Worst
import java.awt.event.MouseListener
import java.awt.event.MouseAdapter
import com.puzzletimer.models.table.CustomTableModel
import javax.swing.table.TableColumn
import com.puzzletimer.models.table.SortButtonRenderer
import javax.swing.table.JTableHeader
import com.puzzletimer.gui.HistoryFrame.HeaderListener
import com.puzzletimer.gui.HistoryFrame.SolutionImporterDialog
import com.puzzletimer.gui.SolutionEditingDialog.SolutionEditingDialogListener
import com.puzzletimer.gui.SolutionEditingDialog
import com.puzzletimer.state.MessageManager
import com.puzzletimer.database.SolutionDAO
import com.puzzletimer.gui.MainFrame.ScrambleViewerPanel
import com.puzzletimer.gui.WrapLayout
import com.puzzletimer.gui.HandImage
import com.puzzletimer.gui.TimeLabel
import javax.swing.ImageIcon
import javax.swing.ToolTipManager
import javax.swing.JCheckBox
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JCheckBoxMenuItem
import javax.swing.ButtonGroup
import javax.swing.JRadioButtonMenuItem
import com.puzzletimer.gui.MainFrame.ScramblePanel
import com.puzzletimer.gui.MainFrame.TimerPanel
import com.puzzletimer.gui.MainFrame.TimesScrollPane
import com.puzzletimer.gui.MainFrame.StatisticsPanel
import com.puzzletimer.gui.TipsFrame
import com.puzzletimer.gui.ScrambleQueueFrame
import com.puzzletimer.gui.HistoryFrame
import com.puzzletimer.gui.SessionSummaryFrame
import com.puzzletimer.gui.StackmatDeveloperFrame
import com.puzzletimer.gui.CategoryManagerFrame
import com.puzzletimer.gui.ColorSchemeFrame
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.Mixer
import com.puzzletimer.gui.MainFrame
import javax.swing.UIManager
import javax.swing.SwingUtilities
import com.puzzletimer.timer.ManualInputTimer
import com.puzzletimer.timer.ControlKeysTimer
import com.puzzletimer.timer.SpaceKeyTimer
import javax.sound.sampled.TargetDataLine
import javax.sound.sampled.AudioSystem
import com.puzzletimer.timer.StackmatTimer
import javax.sound.sampled.LineUnavailableException
import javax.swing.JMenuBar
import java.awt.Image
import javax.sound.sampled.Clip
import java.io.BufferedInputStream
import com.puzzletimer.state.UpdateManager
import org.json.JSONObject
import com.puzzletimer.gui.UpdaterFrame
import java.awt.event.InputEvent
import javax.sound.sampled.Line
import javax.sound.sampled.DataLine
import javax.swing.UIManager.LookAndFeelInfo
import java.awt.Desktop
import com.puzzletimer.util.ExportUtils
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import kotlin.Throws
import java.io.IOException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import java.lang.StringBuilder
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Clipboard
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import com.puzzletimer.gui.StackmatGraphPanel
import java.awt.geom.Rectangle2D
import javax.swing.JProgressBar
import java.lang.Runnable
import java.io.BufferedOutputStream
import java.lang.Process
import java.net.URISyntaxException
import java.awt.FlowLayout
import java.awt.LayoutManager
import java.awt.Insets
import java.util.*

class GraphPanel(solutions: Array<Solution?>?, private val configurationManager: ConfigurationManager) : JPanel() {
    var solutionTimes: ArrayList<Long>? = null
    private var solutionIntervalStart: Long = 0
    private var solutionIntervalEnd: Long = 0
    var startTimes: ArrayList<Long>? = null
    var startIntervalStart: Long = 0
    var startIntervalEnd: Long = 0
    fun setSolutions(solutions: Array<Solution?>?) {
        // apply +2, filter DNFs
        solutionTimes = ArrayList()
        startTimes = ArrayList()
        for (solution in solutions!!) {
            val time = realTime(solution!!, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
            if (time != Long.MAX_VALUE) {
                solutionTimes!!.add(time)
                startTimes!!.add(solution.timing.start.time)
            }
        }

        // define solution times interval size
        if (solutionTimes!!.size == 0) {
            solutionIntervalStart = 17000
            solutionIntervalEnd = 23000
        } else {
            // mean
            var mean: Long = 0
            for (i in solutionTimes!!.indices) {
                mean += solutionTimes!![i]
            }
            mean /= solutionTimes!!.size.toLong()

            // standard deviation
            var variance: Long = 0
            for (i in solutionTimes!!.indices) {
                variance += Math.pow((solutionTimes!![i] - mean).toDouble(), 2.0).toLong()
            }
            variance /= solutionTimes!!.size.toLong()
            val standardDeviation = Math.sqrt(variance.toDouble()).toLong()
            solutionIntervalStart = mean - 3 * Math.max(50, standardDeviation)
            solutionIntervalEnd = mean + 3 * Math.max(50, standardDeviation)
        }

        // define start times interval size
        if (solutionTimes!!.size == 0) {
            val now = Date()
            startIntervalStart = now.time - 5000
            startIntervalEnd = now.time + 5000
        } else if (solutionTimes!!.size == 1) {
            startIntervalStart = startTimes!![0] - 5000
            startIntervalEnd = startTimes!![0] + 5000
        } else {
            startIntervalStart = startTimes!![startTimes!!.size - 1]
            startIntervalEnd = startTimes!![0]
            if (startIntervalStart == startIntervalEnd) {
                startIntervalStart = startTimes!![0] - 5000
                startIntervalEnd = startTimes!![0] + 5000
            }
        }

        // repaint
        repaint()
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2.font = Font("Arial", Font.BOLD, 10)
        val hBase = 16
        val wBase = 45

        // draw vertical line
        g2.drawLine(wBase, height - hBase, width - 1, height - hBase)

        // draw vertical ticks
        val nVerticalTicks = 5
        for (i in 0 until nVerticalTicks) {
            val y = (height - hBase - (i + 0.5) * (height - hBase) / nVerticalTicks).toInt()
            g2.drawLine(wBase - 2, y, wBase + 2, y)
        }

        // draw vertical labels
        val vTickInterval = ((solutionIntervalEnd - solutionIntervalStart) / nVerticalTicks).toDouble()
        for (i in 0 until nVerticalTicks) {
            val value = (solutionIntervalStart + (i + 0.5) * vTickInterval).toLong()
            val label = format(value, configurationManager.getConfiguration("TIMER-PRECISION")!!, false)
            val fontMetrics = g2.fontMetrics
            val width = fontMetrics.stringWidth(label)
            val height = fontMetrics.ascent
            val x = wBase - width - 4
            val y = (getHeight() - hBase - (i + 0.5) * (getHeight() - hBase) / nVerticalTicks + height / 2 - 1).toInt()
            g2.drawString(label, x, y)
        }

        // draw horizontal line
        g2.drawLine(wBase, 0, wBase, height - hBase - 1)

        // draw horizontal ticks
        val nHorizontalTicks = 11
        for (i in 0 until nHorizontalTicks) {
            val x = (wBase + (i + 0.5) * (width - wBase) / nHorizontalTicks).toInt()
            val y = height - hBase
            g2.drawLine(x, y - 2, x, y + 2)
        }

        // draw horizontal labels
        val hTickInterval = (startIntervalEnd - startIntervalStart).toDouble() / nHorizontalTicks
        for (i in 0 until nHorizontalTicks) {
            val value = (startIntervalStart + (i + 0.5) * hTickInterval).toLong()
            var label: String?
            label = if (startIntervalEnd - startIntervalStart < 24 * 60 * 60 * 1000) {
                DateFormat.getTimeInstance(DateFormat.MEDIUM).format(value)
            } else {
                DateFormat.getDateInstance(DateFormat.MEDIUM).format(value)
            }
            val fontMetrics = g2.fontMetrics
            val width = fontMetrics.stringWidth(label)
            val height = fontMetrics.ascent
            val x = (wBase + (i + 0.5) * (getWidth() - wBase) / nHorizontalTicks - width / 2).toInt()
            val y = getHeight() - (hBase - height) / 2
            g2.drawString(label, x, y)
        }

        // draw points
        val nBins = width - wBase
        val bins = ArrayList<ArrayList<Long>>(nBins)
        for (i in 0 until nBins) {
            bins.add(ArrayList())
        }
        for (i in solutionTimes!!.indices) {
            val bin = ((nBins - 1) * (startTimes!![i] - startIntervalStart) / (startIntervalEnd - startIntervalStart)).toInt()
            bins[bin].add(solutionTimes!![i])
        }
        for (i in 0 until nBins) {
            if (bins[i].size > 0) {
                var mean: Long = 0
                for (time in bins[i]) {
                    mean += time
                }
                mean /= bins[i].size.toLong()
                if (mean >= solutionIntervalStart && mean < solutionIntervalEnd) {
                    val x = wBase + i
                    val y = (height - hBase - (height - hBase) * (mean - solutionIntervalStart) / (solutionIntervalEnd - solutionIntervalStart)).toInt()
                    g2.fillRect(x - 2, y - 2, 5, 5)
                }
            }
        }
    }

    init {
        background = Color.WHITE
        setSolutions(solutions)
    }
}