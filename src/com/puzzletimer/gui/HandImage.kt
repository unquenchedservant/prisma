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
import java.util.UUID
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
import java.util.Arrays
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

class HandImage(mirrored: Boolean) : JComponent() {
    private val path: GeneralPath
    private val mirrored: Boolean
    private var pressed: Boolean
    fun setPressed(pressed: Boolean) {
        this.pressed = pressed
        repaint()
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        var size = Math.min(width, height)
        if (pressed) {
            size *= TODO("Could not convert int literal '0.95' to Kotlin")
        }
        g2.color = Color(240, 211, 16)
        g2.fillOval(width / 2 - size / 2, height / 2 - size / 2, size, size)
        val path = GeneralPath(path)
        path.transform(AffineTransform.getScaleInstance((if (mirrored) -1.0 else 1.0) * size, size.toDouble()))
        path.transform(AffineTransform.getTranslateInstance((width / 2).toDouble(), (height / 2).toDouble()))
        g2.color = Color(0, 0, 0)
        g2.fill(path)
    }

    init {
        path = GeneralPath()
        path.moveTo(0.00400, -0.43600)
        path.curveTo(-0.01886, -0.43507, -0.03426, -0.41220, -0.03580, -0.39080)
        path.curveTo(-0.03716, -0.34382, -0.03526, -0.29672, -0.03545, -0.24970)
        path.curveTo(-0.03474, -0.18284, -0.03496, -0.11581, -0.03370, -0.04904)
        path.curveTo(-0.02907, -0.04205, -0.01845, -0.04763, -0.01160, -0.04599)
        path.curveTo(0.00706, -0.04651, 0.02592, -0.04443, 0.04445, -0.04504)
        path.curveTo(0.05201, -0.05002, 0.04626, -0.06156, 0.04795, -0.06899)
        path.curveTo(0.04676, -0.17803, 0.04662, -0.28714, 0.04480, -0.39615)
        path.curveTo(0.04317, -0.41688, 0.02640, -0.43762, 0.00400, -0.43600)
        path.moveTo(-0.08875, -0.41264)
        path.curveTo(-0.11117, -0.41088, -0.12302, -0.38723, -0.12520, -0.36729)
        path.curveTo(-0.12761, -0.33536, -0.12470, -0.30320, -0.12520, -0.27120)
        path.curveTo(-0.12404, -0.19332, -0.12388, -0.11513, -0.12210, -0.03745)
        path.curveTo(-0.11759, -0.02980, -0.10709, -0.03779, -0.10040, -0.03720)
        path.curveTo(-0.08290, -0.04107, -0.06477, -0.04184, -0.04740, -0.04535)
        path.curveTo(-0.04138, -0.05260, -0.04698, -0.06376, -0.04530, -0.07235)
        path.curveTo(-0.04717, -0.17121, -0.04815, -0.27011, -0.05055, -0.36896)
        path.curveTo(-0.05214, -0.38941, -0.06522, -0.41386, -0.08875, -0.41266)
        path.moveTo(0.09765, -0.38291)
        path.curveTo(0.07647, -0.38192, 0.06463, -0.35949, 0.06545, -0.34021)
        path.curveTo(0.06528, -0.24229, 0.06437, -0.14427, 0.06475, -0.04642)
        path.curveTo(0.06893, -0.03894, 0.08019, -0.04321, 0.08715, -0.04077)
        path.curveTo(0.10364, -0.03961, 0.12025, -0.03607, 0.13665, -0.03567)
        path.curveTo(0.14396, -0.04043, 0.13804, -0.05177, 0.13995, -0.05892)
        path.curveTo(0.13958, -0.15312, 0.14041, -0.24738, 0.13930, -0.34157)
        path.curveTo(0.13750, -0.36297, 0.12084, -0.38429, 0.09765, -0.38292)
        path.moveTo(-0.17390, -0.32535)
        path.curveTo(-0.19753, -0.32337, -0.21106, -0.29832, -0.21200, -0.27670)
        path.curveTo(-0.21116, -0.18590, -0.20947, -0.09502, -0.20770, -0.00426)
        path.curveTo(-0.20498, 0.00413, -0.19471, -0.00299, -0.19010, -0.00541)
        path.curveTo(-0.17334, -0.01414, -0.15531, -0.02070, -0.13810, -0.02846)
        path.curveTo(-0.13328, -0.03587, -0.13813, -0.04623, -0.13665, -0.05461)
        path.curveTo(-0.13835, -0.13384, -0.13890, -0.21317, -0.14130, -0.29235)
        path.curveTo(-0.14284, -0.30934, -0.15508, -0.32687, -0.17390, -0.32535)
        path.moveTo(-0.00720, -0.02516)
        path.curveTo(-0.04566, -0.02380, -0.08440, -0.02059, -0.12175, -0.01126)
        path.curveTo(-0.17324, 0.00348, -0.21256, 0.04874, -0.22355, 0.10084)
        path.curveTo(-0.23413, 0.14816, -0.22547, 0.19739, -0.21505, 0.24399)
        path.curveTo(-0.20498, 0.28118, -0.19357, 0.32031, -0.16665, 0.34869)
        path.curveTo(-0.15511, 0.36155, -0.13597, 0.36703, -0.11990, 0.35979)
        path.curveTo(-0.09503, 0.35282, -0.07897, 0.32912, -0.07495, 0.30459)
        path.curveTo(-0.06463, 0.25586, -0.07622, 0.20405, -0.05925, 0.15669)
        path.curveTo(-0.04911, 0.12664, -0.02044, 0.10575, 0.01065, 0.10244)
        path.curveTo(0.03577, 0.09797, 0.06099, 0.10389, 0.08605, 0.10524)
        path.curveTo(0.11284, 0.10622, 0.14057, 0.08999, 0.14705, 0.06299)
        path.curveTo(0.15685, 0.02997, 0.13533, -0.00831, 0.10165, -0.01631)
        path.curveTo(0.06596, -0.02299, 0.02919, -0.02547, -0.00720, -0.02516)
        path.moveTo(0.34205, 0.04119)
        path.curveTo(0.31289, 0.04284, 0.28613, 0.05904, 0.26930, 0.08244)
        path.curveTo(0.24593, 0.11190, 0.22702, 0.14956, 0.18935, 0.16289)
        path.curveTo(0.17843, 0.16584, 0.17184, 0.15359, 0.16255, 0.15029)
        path.curveTo(0.11164, 0.12130, 0.04120, 0.12236, -0.00380, 0.16229)
        path.curveTo(-0.05729, 0.20987, -0.06621, 0.30113, -0.01945, 0.35644)
        path.curveTo(0.00386, 0.38513, 0.04236, 0.39789, 0.07870, 0.39334)
        path.curveTo(0.12655, 0.39102, 0.17234, 0.37106, 0.20775, 0.33914)
        path.curveTo(0.23828, 0.31164, 0.26692, 0.28069, 0.28865, 0.24554)
        path.curveTo(0.31593, 0.19899, 0.33176, 0.14465, 0.36960, 0.10504)
        path.curveTo(0.38301, 0.08995, 0.38743, 0.06296, 0.37000, 0.04909)
        path.curveTo(0.36220, 0.04282, 0.35187, 0.04075, 0.34205, 0.04119)
        this.mirrored = mirrored
        pressed = false
    }
}