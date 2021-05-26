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
import com.puzzletimer.models.ColorScheme.FaceColor
import com.puzzletimer.graphics.Panel3D
import com.puzzletimer.graphics.Vector3
import javax.swing.JLabel
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
import javax.swing.ListModel
import javax.swing.KeyStroke
import javax.swing.JComponent
import com.puzzletimer.puzzles.PuzzleProvider
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.tips.TipProvider
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import com.puzzletimer.gui.CategoryEditorDialog
import javax.swing.JOptionPane
import javax.swing.table.TableCellRenderer
import javax.swing.BorderFactory
import com.puzzletimer.gui.ColorSchemeFrame.ColorRenderer
import javax.swing.JColorChooser
import javax.swing.WindowConstants
import javax.swing.JPanel
import com.puzzletimer.util.SolutionUtils
import java.text.DateFormat
import java.awt.geom.GeneralPath
import java.awt.geom.AffineTransform
import com.puzzletimer.parsers.ScrambleParserProvider
import com.puzzletimer.parsers.ScrambleParser
import com.puzzletimer.gui.HistoryFrame.SolutionImporterDialog.SolutionImporterListener
import javax.swing.JTextArea
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
import com.puzzletimer.models.table.CustomTableModel
import javax.swing.table.TableColumn
import com.puzzletimer.models.table.SortButtonRenderer
import javax.swing.table.JTableHeader
import com.puzzletimer.gui.HistoryFrame.HeaderListener
import com.puzzletimer.gui.HistoryFrame.SolutionImporterDialog
import com.puzzletimer.gui.SolutionEditingDialog.SolutionEditingDialogListener
import com.puzzletimer.gui.SolutionEditingDialog
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
import javax.sound.sampled.Clip
import java.io.BufferedInputStream
import org.json.JSONObject
import com.puzzletimer.gui.UpdaterFrame
import javax.sound.sampled.Line
import javax.sound.sampled.DataLine
import javax.swing.UIManager.LookAndFeelInfo
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
import com.puzzletimer.gui.StackmatGraphPanel
import com.puzzletimer.models.*
import com.puzzletimer.state.*
import net.miginfocom.swing.MigLayout
import java.awt.*
import java.awt.geom.Rectangle2D
import javax.swing.JProgressBar
import java.lang.Runnable
import java.io.BufferedOutputStream
import java.lang.Process
import java.net.URISyntaxException
import java.awt.event.*
import java.util.*

class HistoryFrame(
        scramblerProvider: ScramblerProvider,
        scrambleParserProvider: ScrambleParserProvider,
        categoryManager: CategoryManager,
        scrambleManager: ScrambleManager?,
        solutionManager: SolutionManager,
        sessionManager: SessionManager,
        timerManager: TimerManager,
        private val configurationManager: ConfigurationManager) : JFrame() {
    private class SolutionImporterDialog(
            owner: JFrame?,
            modal: Boolean,
            categoryId: UUID?,
            scramblerId: String?,
            scrambleParser: ScrambleParser?,
            listener: SolutionImporterListener) : JDialog(owner, modal) {
        open class SolutionImporterListener {
            open fun solutionsImported(solutions: Array<Solution?>?) {}
        }

        private var textAreaContents: JTextArea? = null
        private var buttonOk: JButton? = null
        private var buttonCancel: JButton? = null
        private fun createComponents() {
            layout = MigLayout(
                    "fill",
                    "",
                    "[pref!][fill]16[pref!]")

            // labelSolutions
            add(JLabel(_("solution_importer.solutions")), "wrap")

            // textAreaContents
            textAreaContents = JTextArea(_("solution_importer.default_contents"))
            val scrollPane = JScrollPane(textAreaContents)
            scrollPane.preferredSize = Dimension(0, 0)
            add(scrollPane, "growx, wrap")

            // buttonOk
            buttonOk = JButton(_("solution_importer.ok"))
            add(buttonOk, "right, width 100, span 2, split")

            // buttonCancel
            buttonCancel = JButton(_("solution_importer.cancel"))
            add(buttonCancel, "width 100")
        }

        init {
            title = _("solution_importer.solution_importer")
            minimumSize = Dimension(640, 480)
            createComponents()
            pack()

            // ok button
            buttonOk!!.addActionListener { event: ActionEvent? ->
                val contents = textAreaContents!!.text
                var start = Date()
                val solutions = ArrayList<Solution>()
                for (line in contents.split("\n").toTypedArray()) {
                    line = line.trim { it <= ' ' }

                    // ignore blank lines and comments
                    if (line.length == 0 || line.startsWith("#")) {
                        continue
                    }

                    // separate time from scramble
                    val parts = line.split("\\s+", 2.toBoolean()).toTypedArray()

                    // time
                    val time = parseTime(parts[0])
                    val timing = Timing(
                            start,
                            Date(start.time + time))

                    // scramble
                    var scramble = Scramble(scramblerId!!, arrayOfNulls(0))
                    if (parts.size > 1) {
                        scramble = Scramble(
                                scramblerId,
                                scrambleParser!!.parse(parts[1]))
                    }
                    solutions.add(
                            Solution(
                                    UUID.randomUUID(),
                                    categoryId!!,
                                    scramble,
                                    timing,
                                    "",
                                    ""))
                    start = Date(start.time + time)
                }
                val solutionsArray = arrayOfNulls<Solution>(solutions.size)
                solutions.toArray(solutionsArray)
                listener.solutionsImported(solutionsArray)
                dispose()
            }

            // cancel button
            buttonCancel!!.addActionListener { event: ActionEvent? -> dispose() }

            // esc key closes window
            getRootPane().registerKeyboardAction(
                    { arg0: ActionEvent? -> dispose() },
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW)
        }
    }

    private var histogramPanel: HistogramPanel? = null
    private var graphPanel: GraphPanel? = null
    private var labelNumberOfSolutions: JLabel? = null
    private var labelMean: JLabel? = null
    private var labelBest: JLabel? = null
    private var labelMeanOf3: JLabel? = null
    private var labelBestMeanOf3: JLabel? = null
    private var labelAverage: JLabel? = null
    private var labelLowerQuartile: JLabel? = null
    private var labelMeanOf10: JLabel? = null
    private var labelBestMeanOf10: JLabel? = null
    private var labelInterquartileMean: JLabel? = null
    private var labelMedian: JLabel? = null
    private var labelMeanOf100: JLabel? = null
    private var labelBestMeanOf100: JLabel? = null
    private var labelUpperQuartile: JLabel? = null
    private var labelAverageOf5: JLabel? = null
    private var labelBestAverageOf5: JLabel? = null
    private var labelStandardDeviation: JLabel? = null
    private var labelWorst: JLabel? = null
    private var labelAverageOf12: JLabel? = null
    private var labelBestAverageOf12: JLabel? = null
    private var labelAverageOf50: JLabel? = null
    private var labelBestAverageOf50: JLabel? = null
    private var table: JTable? = null
    private var buttonAddSolutions: JButton? = null
    private var buttonEdit: JButton? = null
    private var buttonRemove: JButton? = null
    private var buttonSelectSession: JButton? = null
    private var buttonSelectNone: JButton? = null
    private var buttonOk: JButton? = null
    private var nullTime = "XX:XX.XX"
    private fun createComponents() {
        layout = MigLayout(
                "fill",
                "[][pref!]",
                "[pref!][pref!]12[pref!][pref!]12[pref!][pref!]12[pref!][]16[pref!]")

        // labelHistogram
        add(JLabel(_("history.histogram")), "span, wrap")

        // histogram
        histogramPanel = HistogramPanel(arrayOfNulls(0), 17, configurationManager)
        add(histogramPanel, "growx, height 90, span, wrap")

        // labelGraph
        add(JLabel(_("history.graph")), "span, wrap")

        // Graph
        graphPanel = GraphPanel(arrayOfNulls(0), configurationManager)
        add(graphPanel, "growx, height 90, span, wrap")

        // labelStatistics
        add(JLabel(_("history.statistics")), "span, wrap")

        // panelStatistics
        val panelStatistics = JPanel(
                MigLayout(
                        "fill, insets 0 n 0 n",
                        "[][pref!]32[][pref!]32[][pref!]32[][pref!]",
                        "[pref!]1[pref!]1[pref!]1[pref!]1[pref!]1[pref!]"))
        add(panelStatistics, "growx, span, wrap")

        // labelNumberOfSolutions
        panelStatistics.add(JLabel(_("history.number_of_solutions")), "")
        labelNumberOfSolutions = JLabel("")
        panelStatistics.add(labelNumberOfSolutions, "right")

        // labelBest
        panelStatistics.add(JLabel(_("history.best")), "")
        labelBest = JLabel(nullTime)
        panelStatistics.add(labelBest, "right")

        // labelMeanOf3
        panelStatistics.add(JLabel(_("history.mean_of_3")), "")
        labelMeanOf3 = JLabel(nullTime)
        panelStatistics.add(labelMeanOf3, "right")

        // labelBestMeanOf3
        panelStatistics.add(JLabel(_("history.best_mean_of_3")), "")
        labelBestMeanOf3 = JLabel(nullTime)
        panelStatistics.add(labelBestMeanOf3, "right, wrap")

        // labelMean
        panelStatistics.add(JLabel(_("history.mean")), "")
        labelMean = JLabel(nullTime)
        panelStatistics.add(labelMean, "right")

        // labelLowerQuartile
        panelStatistics.add(JLabel(_("history.lower_quartile")), "")
        labelLowerQuartile = JLabel(nullTime)
        panelStatistics.add(labelLowerQuartile, "right")

        // labelMeanOf10
        panelStatistics.add(JLabel(_("history.mean_of_10")), "")
        labelMeanOf10 = JLabel(nullTime)
        panelStatistics.add(labelMeanOf10, "right")

        // labelBestMeanOf10
        panelStatistics.add(JLabel(_("history.best_mean_of_10")), "")
        labelBestMeanOf10 = JLabel(nullTime)
        panelStatistics.add(labelBestMeanOf10, "right, wrap")

        // labelAverage
        panelStatistics.add(JLabel(_("history.average")), "")
        labelAverage = JLabel(nullTime)
        panelStatistics.add(labelAverage, "right")

        // labelMedian
        panelStatistics.add(JLabel(_("history.median")), "")
        labelMedian = JLabel(nullTime)
        panelStatistics.add(labelMedian, "right")

        // labelMeanOf100
        panelStatistics.add(JLabel(_("history.mean_of_100")), "")
        labelMeanOf100 = JLabel(nullTime)
        panelStatistics.add(labelMeanOf100, "right")

        // labelBestMeanOf100
        panelStatistics.add(JLabel(_("history.best_mean_of_100")), "")
        labelBestMeanOf100 = JLabel(nullTime)
        panelStatistics.add(labelBestMeanOf100, "right, wrap")

        // labelInterquartileMean
        panelStatistics.add(JLabel(_("history.interquartile_mean")), "")
        labelInterquartileMean = JLabel(nullTime)
        panelStatistics.add(labelInterquartileMean, "right")

        // labelUpperQuartile
        panelStatistics.add(JLabel(_("history.upper_quartile")), "")
        labelUpperQuartile = JLabel(nullTime)
        panelStatistics.add(labelUpperQuartile, "right")

        // labelAverageOf5
        panelStatistics.add(JLabel(_("history.average_of_5")), "")
        labelAverageOf5 = JLabel(nullTime)
        panelStatistics.add(labelAverageOf5, "right")

        // labelBestAverageOf5
        panelStatistics.add(JLabel(_("history.best_average_of_5")), "")
        labelBestAverageOf5 = JLabel(nullTime)
        panelStatistics.add(labelBestAverageOf5, "right, wrap")

        // labelStandardDeviation
        panelStatistics.add(JLabel(_("history.standard_deviation")), "")
        labelStandardDeviation = JLabel(nullTime)
        panelStatistics.add(labelStandardDeviation, "right")

        // labelWorst
        panelStatistics.add(JLabel(_("history.worst")), "")
        labelWorst = JLabel(nullTime)
        panelStatistics.add(labelWorst, "right")

        // labelAverageOf12
        panelStatistics.add(JLabel(_("history.average_of_12")), "")
        labelAverageOf12 = JLabel(nullTime)
        panelStatistics.add(labelAverageOf12, "right")

        // labelBestAverageOf12
        panelStatistics.add(JLabel(_("history.best_average_of_12")), "")
        labelBestAverageOf12 = JLabel(nullTime)
        panelStatistics.add(labelBestAverageOf12, "right, wrap")

        // labelAverageOf50
        panelStatistics.add(JLabel(_("history.average_of_50")), "skip 4")
        labelAverageOf50 = JLabel(nullTime)
        panelStatistics.add(labelAverageOf50, "right")

        // labelBestAverageOf50
        panelStatistics.add(JLabel(_("history.best_average_of_50")), "")
        labelBestAverageOf50 = JLabel(nullTime)
        panelStatistics.add(labelBestAverageOf50, "right")

        // labelSolutions
        val labelTimes = JLabel(_("history.solutions"))
        add(labelTimes, "span, wrap")

        // table
        table = JTable()
        table!!.showVerticalLines = false
        val scrollPane = JScrollPane(table)
        table!!.fillsViewportHeight = true
        scrollPane.preferredSize = Dimension(0, 0)
        add(scrollPane, "grow")

        // buttonAddSolutions
        buttonAddSolutions = JButton(_("history.add_solutions"))
        add(buttonAddSolutions, "growx, top, split 5, flowy")

        // buttonEdit
        buttonEdit = JButton(_("history.edit"))
        buttonEdit!!.isEnabled = false
        add(buttonEdit, "growx, top")

        // buttonRemove
        buttonRemove = JButton(_("history.remove"))
        buttonRemove!!.isEnabled = false
        add(buttonRemove, "growx, top")

        // buttonSelectSession
        buttonSelectSession = JButton(_("history.select_session"))
        add(buttonSelectSession, "growx, top, gaptop 16")

        // buttonSelectNone
        buttonSelectNone = JButton(_("history.select_none"))
        add(buttonSelectNone, "growx, top, wrap")

        // buttonOk
        buttonOk = JButton(_("history.ok"))
        add(buttonOk, "tag ok, span")
    }

    private fun updateStatistics(solutions: Array<Solution?>?, selectedRows: IntArray) {
        labelNumberOfSolutions!!.text = Integer.toString(solutions!!.size)
        val labels = arrayOf(
                labelBest,
                labelMeanOf3,
                labelBestMeanOf3,
                labelMean,
                labelLowerQuartile,
                labelMeanOf10,
                labelBestMeanOf10,
                labelAverage,
                labelMedian,
                labelMeanOf100,
                labelBestMeanOf100,
                labelInterquartileMean,
                labelUpperQuartile,
                labelAverageOf5,
                labelBestAverageOf5,
                labelStandardDeviation,
                labelWorst,
                labelAverageOf12,
                labelBestAverageOf12,
                labelAverageOf50,
                labelBestAverageOf50)
        val measures = arrayOf(
                Best(1, Int.MAX_VALUE),
                Mean(3, 3),
                BestMean(3, Int.MAX_VALUE),
                Mean(1, Int.MAX_VALUE),
                Percentile(1, Int.MAX_VALUE, 0.25),
                Mean(10, 10),
                BestMean(10, Int.MAX_VALUE),
                Average(3, Int.MAX_VALUE),
                Percentile(1, Int.MAX_VALUE, 0.5),
                Mean(100, 100),
                BestMean(100, Int.MAX_VALUE),
                InterquartileMean(3, Int.MAX_VALUE),
                Percentile(1, Int.MAX_VALUE, 0.75),
                Average(5, 5),
                BestAverage(5, Int.MAX_VALUE),
                StandardDeviation(1, Int.MAX_VALUE),
                Worst(1, Int.MAX_VALUE),
                Average(12, 12),
                BestAverage(12, Int.MAX_VALUE),
                Average(50, 50),
                BestAverage(50, Int.MAX_VALUE))
        val clickable = booleanArrayOf(
                true,
                true,
                true,
                false,
                false,
                true,
                true,
                false,
                false,
                true,
                true,
                false,
                false,
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                true)
        for (i in labels.indices) {
            if (solutions.size >= measures[i].minimumWindowSize) {
                val size = Math.min(solutions.size, measures[i].maximumWindowSize)
                val window = arrayOfNulls<Solution>(size)
                System.arraycopy(solutions, 0, window, 0, size)
                measures[i].setSolutions(window, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
                labels[i]!!.text = formatMinutes(measures[i].value, configurationManager.getConfiguration("TIMER-PRECISION")!!, measures[i].round)
            } else {
                labels[i]!!.text = nullTime
            }
            if (clickable[i]) {
                val mouseListeners = labels[i]!!.mouseListeners
                for (mouseListener in mouseListeners) {
                    labels[i]!!.removeMouseListener(mouseListener)
                }
                labels[i]!!.cursor = Cursor(Cursor.DEFAULT_CURSOR)
                if (solutions.size >= measures[i].minimumWindowSize) {
                    labels[i]!!.cursor = Cursor(Cursor.HAND_CURSOR)
                    val windowSize = measures[i].minimumWindowSize
                    val windowPosition = measures[i].windowPosition
                    labels[i]!!.addMouseListener(object : MouseAdapter() {
                        override fun mouseClicked(e: MouseEvent) {
                            if (table!!.rowCount > 0) {
                                table!!.removeRowSelectionInterval(
                                        0,
                                        table!!.rowCount - 1)
                            }
                            for (i in 0 until windowSize) {
                                table!!.addRowSelectionInterval(
                                        selectedRows[windowPosition + i],
                                        selectedRows[windowPosition + i])
                                table!!.scrollRectToVisible(table!!.getCellRect(selectedRows[windowPosition + i], 0, true))
                            }
                        }
                    })
                }
            }
        }
    }

    private fun updateTable(solutions: Array<Solution?>?) {
        val tableModel: CustomTableModel = object : CustomTableModel() {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                return false
            }
        }
        tableModel.addColumn(_("history.#"))
        tableModel.addColumn(_("history.start"))
        tableModel.addColumn(_("history.time"))
        tableModel.addColumn(_("history.penalty"))
        tableModel.addColumn(_("history.comment"))
        tableModel.addColumn(_("history.scramble"))
        table!!.model = tableModel
        table!!.autoResizeMode = JTable.AUTO_RESIZE_LAST_COLUMN
        val columnsWidth = intArrayOf(100, 400, 200, 200, 300, 1000)
        for (i in columnsWidth.indices) {
            val indexColumn = table!!.columnModel.getColumn(i)
            indexColumn.preferredWidth = columnsWidth[i]
        }
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
        for (i in solutions!!.indices) {
            // start
            val sStart = dateFormat.format(solutions[i]!!.timing.start)

            // time
            val sTime = formatMinutes(solutions[i]!!.timing.elapsedTime, configurationManager.getConfiguration("TIMER-PRECISION")!!, false)
            tableModel.addRow(arrayOf<Any>(
                    solutions.size - i,
                    sStart,
                    sTime,
                    solutions[i]!!.penalty,
                    solutions[i]!!.comment,
                    solutions[i]!!.scramble.rawSequence))
        }
        val renderer = SortButtonRenderer()
        val header = table!!.tableHeader
        header.addMouseListener(HeaderListener(header, renderer))
    }

    internal inner class HeaderListener(var header: JTableHeader, var renderer: SortButtonRenderer) : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            val col = header.columnAtPoint(e.point)
            val sortCol = header.table.convertColumnIndexToModel(col)
            renderer.setPressedColumn(col)
            renderer.setSelectedColumn(col)
            header.repaint()
            if (header.table.isEditing) {
                header.table.cellEditor.stopCellEditing()
            }
            val isAscent: Boolean
            isAscent = SortButtonRenderer.DOWN == renderer.getState(col)
            (header.table.model as CustomTableModel)
                    .sortByColumn(sortCol, isAscent)
        }

        override fun mouseReleased(e: MouseEvent) {
            val col = header.columnAtPoint(e.point)
            renderer.setPressedColumn(-1) // clear
            header.repaint()
        }
    }

    init {
        minimumSize = Dimension(800, 600)
        createComponents()
        pack()

        // title
        categoryManager.addListener(object : CategoryManager.Listener() {
            override fun categoriesUpdated(categories: Array<Category>?, currentCategory: Category?) {
                title = java.lang.String.format(
                        _("history.history_category"),
                        currentCategory!!.getDescription())
            }
        })
        categoryManager.notifyListeners()
        timerManager.addListener(object : TimerManager.Listener() {
            override fun precisionChanged(timerPrecisionId: String?) {
                val solutions = solutionManager.getSolutions()
                val selectedRows = IntArray(solutions.size)
                for (i in selectedRows.indices) {
                    selectedRows[i] = i
                }
                if (timerPrecisionId == "CENTISECONDS") {
                    nullTime = "XX:XX.XX"
                } else if (timerPrecisionId == "MILLISECONDS") {
                    nullTime = "XX:XX.XXX"
                }
                updateStatistics(solutions, selectedRows)
                updateTable(solutions)
            }
        })

        // statistics, table
        solutionManager.addListener(object : SolutionManager.Listener() {
            override fun solutionsUpdated(solutions: Array<Solution?>?) {
                val selectedRows = IntArray(solutions!!.size)
                for (i in selectedRows.indices) {
                    selectedRows[i] = i
                }
                histogramPanel!!.setSolutions(solutions)
                graphPanel!!.setSolutions(solutions)
                updateStatistics(solutions, selectedRows)
                updateTable(solutions)
            }
        })
        solutionManager.notifyListeners()

        // table selection
        table!!.selectionModel.addListSelectionListener { event: ListSelectionEvent? ->
            val solutions = solutionManager.getSolutions()
            val selectedSolutions: Array<Solution?>
            var selectedRows = table!!.selectedRows
            if (selectedRows.size <= 0) {
                selectedRows = IntArray(table!!.rowCount)
                for (i in selectedRows.indices) {
                    selectedRows[i] = i
                }
                selectedSolutions = solutions
            } else {
                selectedSolutions = arrayOfNulls(selectedRows.size)
                for (i in selectedSolutions.indices) {
                    selectedSolutions[i] = solutions[selectedRows[i]]
                }
            }
            histogramPanel!!.setSolutions(selectedSolutions)
            graphPanel!!.setSolutions(selectedSolutions)
            updateStatistics(selectedSolutions, selectedRows)
            buttonEdit!!.isEnabled = table!!.selectedRowCount == 1
            buttonRemove!!.isEnabled = table!!.selectedRowCount > 0
        }

        // add solutions button
        buttonAddSolutions!!.addActionListener { e: ActionEvent? ->
            val listener: SolutionImporterListener = object : SolutionImporterListener() {
                override fun solutionsImported(solutions: Array<Solution?>?) {
                    solutionManager.addSolutions(solutions)
                }
            }
            val currentCategory = categoryManager.getCurrentCategory()
            val currentScrambler = scramblerProvider[currentCategory.getScramblerId()!!]
            val puzzleId = currentScrambler!!.scramblerInfo.puzzleId
            val scrambleParser = scrambleParserProvider[puzzleId]
            val solutionEditingDialog = SolutionImporterDialog(
                    this@HistoryFrame,
                    true,
                    currentCategory.getCategoryId(),
                    currentCategory.getScramblerId(),
                    scrambleParser,
                    listener)
            solutionEditingDialog.setLocationRelativeTo(null)
            solutionEditingDialog.isVisible = true
        }

        // edit button
        buttonEdit!!.addActionListener { e: ActionEvent? ->
            val solutions = solutionManager.getSolutions()
            val solution = solutions[table!!.selectedRow]
            val listener: SolutionEditingDialogListener = object : SolutionEditingDialogListener() {
                override fun solutionEdited(solution: Solution?) {
                    solutionManager.updateSolution(solution!!)
                }
            }
            val solutionEditingDialog = SolutionEditingDialog(
                    this@HistoryFrame,
                    true,
                    solution,
                    listener,
                    configurationManager)
            solutionEditingDialog.setLocationRelativeTo(null)
            solutionEditingDialog.isVisible = true
        }

        // remove button
        buttonRemove!!.addActionListener { e: ActionEvent? ->
            if (table!!.selectedRows.size > 5) {
                val result = JOptionPane.showConfirmDialog(
                        this@HistoryFrame,
                        _("history.solution_removal_confirmation_message"),
                        _("history.remove_solutions"),
                        JOptionPane.YES_NO_CANCEL_OPTION)
                if (result != JOptionPane.YES_OPTION) {
                    return@addActionListener
                }
            }
            val solutions = solutionManager.getSolutions()
            val selectedRows = table!!.selectedRows
            for (i in selectedRows.indices) {
                solutionManager.removeSolution(solutions[selectedRows[i]]!!)
            }

            // request focus
            buttonRemove!!.requestFocusInWindow()
        }

        // select session button
        buttonSelectSession!!.addActionListener { e: ActionEvent? ->
            if (table!!.rowCount > 0) {
                table!!.removeRowSelectionInterval(
                        0,
                        table!!.rowCount - 1)
            }
            val solutions = solutionManager.getSolutions()
            val sessionSolutions = sessionManager.getSolutions()
            var i = 0
            var j = 0
            while (i < solutions.size && j < sessionSolutions.size) {
                if (solutions[i]!!.solutionId == sessionSolutions[j]!!.solutionId) {
                    table!!.addRowSelectionInterval(i, i)
                    table!!.scrollRectToVisible(table!!.getCellRect(i, 0, true))
                    j++
                }
                i++
            }
        }

        // select none button
        buttonSelectNone!!.addActionListener { e: ActionEvent? ->
            if (table!!.rowCount > 0) {
                table!!.removeRowSelectionInterval(
                        0,
                        table!!.rowCount - 1)
            }
        }

        // close button
        defaultCloseOperation = HIDE_ON_CLOSE
        buttonOk!!.addActionListener { arg0: ActionEvent? -> this@HistoryFrame.isVisible = false }

        // esc key closes window
        getRootPane().registerKeyboardAction(
                { arg0: ActionEvent? -> this@HistoryFrame.isVisible = false },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW)
    }
}