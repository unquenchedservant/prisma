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
import com.puzzletimer.models.*
import net.miginfocom.swing.MigLayout
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

class ScrambleQueueFrame(
        scrambleParserProvider: ScrambleParserProvider,
        scramblerProvider: ScramblerProvider,
        categoryManager: CategoryManager,
        scrambleManager: ScrambleManager) : JFrame() {
    private var table: JTable? = null
    private var buttonUp: JButton? = null
    private var buttonDown: JButton? = null
    private var buttonRemove: JButton? = null
    private var buttonImportFromFile: JButton? = null
    private var buttonExport: JButton? = null
    private var comboBoxScrambler: JComboBox<*>? = null
    private var spinnerNumberOfScrambles: JSpinner? = null
    private var buttonImportFromScrambler: JButton? = null
    private var buttonOk: JButton? = null
    private fun createComponents() {
        layout = MigLayout(
                "fill",
                "[fill][pref!]",
                "[pref!][]12[pref!][pref!]16[pref!]")

        // labelQueue
        add(JLabel(_("scramble_queue.queue")), "span, wrap")

        // table
        table = JTable()
        table!!.showVerticalLines = false
        val scrollPane = JScrollPane(table)
        table!!.fillsViewportHeight = true
        scrollPane.preferredSize = Dimension(0, 0)
        add(scrollPane, "grow")

        // buttonUp
        buttonUp = JButton(_("scramble_queue.up"))
        buttonUp!!.isEnabled = false
        add(buttonUp, "top, growx, split 5, flowy")

        // buttonDown
        buttonDown = JButton(_("scramble_queue.down"))
        buttonDown!!.isEnabled = false
        add(buttonDown, "top, growx")

        // buttonRemove
        buttonRemove = JButton(_("scramble_queue.remove"))
        buttonRemove!!.isEnabled = false
        add(buttonRemove, "top, growx")

        // buttonImportFromFile
        buttonImportFromFile = JButton(_("scramble_queue.import_from_file"))
        add(buttonImportFromFile, "gaptop 20, top, growx")

        // buttonExport
        buttonExport = JButton(_("scramble_queue.export_to_file"))
        buttonExport!!.isEnabled = false
        add(buttonExport, "top, growx, wrap")

        // labelImportFromScrambler
        add(JLabel(_("scramble_queue.import_from_scrambler")), "span, wrap")

        // comboBoxScrambler
        comboBoxScrambler = JComboBox<Any?>()
        add(comboBoxScrambler, "growx, span, split 3")

        // spinnerNumberOfScrambles
        spinnerNumberOfScrambles = JSpinner(SpinnerNumberModel(12, 1, 1000, 1))
        add(spinnerNumberOfScrambles, "")

        // buttonImportFromScrambler
        buttonImportFromScrambler = JButton(_("scramble_queue.import"))
        add(buttonImportFromScrambler, "wrap")

        // buttonOK
        buttonOk = JButton(_("scramble_queue.ok"))
        add(buttonOk, "tag ok, span")
    }

    private fun updateTable(queue: Array<Scramble?>?) {
        val tableModel: DefaultTableModel = object : DefaultTableModel() {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                return false
            }
        }
        tableModel.addColumn(_("scramble_queue.#"))
        tableModel.addColumn(_("scramble_queue.scramble"))
        table!!.model = tableModel
        table!!.autoResizeMode = JTable.AUTO_RESIZE_LAST_COLUMN
        val indexColumn = table!!.columnModel.getColumn(0)
        indexColumn.preferredWidth = 50
        val scrambleColumn = table!!.columnModel.getColumn(1)
        scrambleColumn.preferredWidth = 1000
        for (i in queue!!.indices) {
            tableModel.addRow(arrayOf<Any>(
                    i + 1,
                    queue[i]!!.rawSequence))
        }
    }

    private fun updateButtons(table: JTable?) {
        val selectedRows = table!!.selectedRows
        val nRows = table.rowCount

        // up button
        buttonUp!!.isEnabled = selectedRows.size > 0 &&
                selectedRows[0] != 0

        // down button
        buttonDown!!.isEnabled = selectedRows.size > 0 &&
                selectedRows[selectedRows.size - 1] != nRows - 1

        // remove button
        buttonRemove!!.isEnabled = selectedRows.size > 0

        // export button
        buttonExport!!.isEnabled = nRows > 0
    }

    @Throws(IOException::class)
    private fun loadScramblesFromFile(file: File, scramblerId: String, scrambleParser: ScrambleParser?): Array<Scramble?> {
        val fileInputStream = FileInputStream(file)
        val scanner = Scanner(fileInputStream, "UTF-8")
        val scrambles = ArrayList<Scramble>()
        while (scanner.hasNextLine()) {
            scrambles.add(
                    Scramble(
                            scramblerId,
                            scrambleParser!!.parse(scanner.nextLine().trim { it <= ' ' })))
        }
        scanner.close()
        val scrambleArray = arrayOfNulls<Scramble>(scrambles.size)
        scrambles.toArray(scrambleArray)
        return scrambleArray
    }

    @Throws(IOException::class)
    private fun saveScramblesToFile(scrambles: Array<Scramble?>, file: File) {
        val fileOutputStream = FileOutputStream(file)
        val writer = OutputStreamWriter(fileOutputStream, "UTF-8")
        for (scramble in scrambles) {
            writer.append(scramble!!.rawSequence)
            writer.append("\r\n")
        }
        writer.close()
    }

    init {
        minimumSize = Dimension(640, 480)
        createComponents()
        pack()

        // on category change
        categoryManager.addListener(object : CategoryManager.Listener() {
            override fun categoriesUpdated(categories: Array<Category>?, currentCategory: Category?) {
                // title
                title = java.lang.String.format(
                        _("scramble_queue.scramble_queue-category"),
                        currentCategory!!.getDescription())

                // scrambler combobox
                comboBoxScrambler!!.removeAllItems()
                val currentScrambler = scramblerProvider[categoryManager.getCurrentCategory().getScramblerId()!!]
                val puzzleId = currentScrambler!!.scramblerInfo.puzzleId
                for (scrambler in scramblerProvider.all) {
                    if (scrambler!!.scramblerInfo.puzzleId == puzzleId) {
                        comboBoxScrambler!!.addItem(scrambler)
                    }
                }
            }
        })
        categoryManager.notifyListeners()

        // on queue update
        scrambleManager.addListener(object : ScrambleManager.Listener() {
            override fun scrambleQueueUpdated(queue: Array<Scramble?>?) {
                updateTable(queue)
                updateButtons(table)
            }
        })

        // enable/disable buttons
        table!!.selectionModel.addListSelectionListener { event: ListSelectionEvent? -> updateButtons(table) }

        // up button
        buttonUp!!.addActionListener { event: ActionEvent? ->
            val table1 = table
            val selectedRows = table1!!.selectedRows

            // move scrambles
            scrambleManager.moveScramblesUp(selectedRows)

            // fix selection
            table1.removeRowSelectionInterval(0, selectedRows.size - 1)
            for (selectedRow in selectedRows) {
                table1.addRowSelectionInterval(selectedRow - 1, selectedRow - 1)
            }

            // request focus
            buttonUp!!.requestFocusInWindow()
        }

        // down button
        buttonDown!!.addActionListener { event: ActionEvent? ->
            val table1 = table
            val selectedRows = table1!!.selectedRows

            // move scrambles
            scrambleManager.moveScramblesDown(selectedRows)

            // fix selection
            table1.removeRowSelectionInterval(0, selectedRows.size - 1)
            for (selectedRow in selectedRows) {
                table1.addRowSelectionInterval(selectedRow + 1, selectedRow + 1)
            }

            // request focus
            buttonDown!!.requestFocusInWindow()
        }

        // remove button
        buttonRemove!!.addActionListener { event: ActionEvent? ->
            // remove scrambles
            scrambleManager.removeScrambles(
                    table!!.selectedRows)

            // request focus
            buttonRemove!!.requestFocusInWindow()
        }

        // import from file
        buttonImportFromFile!!.addActionListener { event: ActionEvent? ->
            val fileChooser = JFileChooser()
            fileChooser.fileFilter = FileNameExtensionFilter(_("scramble_queue.scramble_file_description"), "txt")
            val action = fileChooser.showOpenDialog(this@ScrambleQueueFrame)
            if (action != JFileChooser.APPROVE_OPTION) {
                return@addActionListener
            }
            val category = categoryManager.getCurrentCategory()
            val scrambler = scramblerProvider[category.getScramblerId()!!]
            val puzzleId = scrambler!!.scramblerInfo.puzzleId
            val scrambleParser = scrambleParserProvider[puzzleId]
            val scrambles: Array<Scramble?>
            scrambles = try {
                loadScramblesFromFile(
                        fileChooser.selectedFile,
                        "$puzzleId-IMPORTER",
                        scrambleParser)
            } catch (e: IOException) {
                JOptionPane.showMessageDialog(
                        this@ScrambleQueueFrame,
                        java.lang.String.format(
                                _("scramble_queue.file_opening_error"),
                                fileChooser.selectedFile.absolutePath),
                        _("scramble_queue.error"),
                        JOptionPane.ERROR_MESSAGE)
                return@addActionListener
            }
            scrambleManager.addScrambles(scrambles, false)
        }

        // export to file
        buttonExport!!.addActionListener { event: ActionEvent? ->
            val fileChooser = JFileChooser()
            fileChooser.selectedFile = File(_("scramble_queue.default_file_name"))
            fileChooser.fileFilter = FileNameExtensionFilter(_("scramble_queue.scramble_file_description"), "txt")
            val action = fileChooser.showSaveDialog(this@ScrambleQueueFrame)
            if (action != JFileChooser.APPROVE_OPTION) {
                return@addActionListener
            }
            val scrambles = scrambleManager.getQueue()
            val selectedScrambles: Array<Scramble?>
            val selectedRows = table!!.selectedRows
            if (selectedRows.size <= 0) {
                selectedScrambles = scrambles
            } else {
                selectedScrambles = arrayOfNulls(selectedRows.size)
                for (i in selectedScrambles.indices) {
                    selectedScrambles[i] = scrambles[selectedRows[i]]
                }
            }
            try {
                saveScramblesToFile(
                        selectedScrambles,
                        fileChooser.selectedFile)
            } catch (e: IOException) {
                JOptionPane.showMessageDialog(
                        this@ScrambleQueueFrame,
                        java.lang.String.format(
                                _("scramble_queue.file_opening_error"),
                                fileChooser.selectedFile.absolutePath),
                        _("scramble_queue.error"),
                        JOptionPane.ERROR_MESSAGE)
            }
        }

        // import from scrambler
        buttonImportFromScrambler!!.addActionListener { event: ActionEvent? ->
            val scrambler = comboBoxScrambler!!.selectedItem as Scrambler
            val scrambles = arrayOfNulls<Scramble>((spinnerNumberOfScrambles!!.value as Int))
            for (i in scrambles.indices) {
                scrambles[i] = scrambler.nextScramble
            }
            scrambleManager.addScrambles(scrambles, false)
        }

        // ok button
        defaultCloseOperation = HIDE_ON_CLOSE
        buttonOk!!.addActionListener { event: ActionEvent? -> this@ScrambleQueueFrame.isVisible = false }

        // esc key closes window
        getRootPane().registerKeyboardAction(
                { arg0: ActionEvent? -> this@ScrambleQueueFrame.isVisible = false },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW)
    }
}