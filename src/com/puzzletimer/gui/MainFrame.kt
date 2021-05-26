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
import java.lang.Exception
import java.net.URI
import java.util.*

class MainFrame(
        private val messageManager: MessageManager,
        private val configurationManager: ConfigurationManager,
        private val timerManager: TimerManager,
        private val puzzleProvider: PuzzleProvider,
        private val colorManager: ColorManager,
        private val scrambleParserProvider: ScrambleParserProvider,
        private val scramblerProvider: ScramblerProvider,
        private val tipProvider: TipProvider,
        private val categoryManager: CategoryManager,
        private val scrambleManager: ScrambleManager,
        private val solutionManager: SolutionManager,
        private val sessionManager: SessionManager,
        private val solutionDAO: SolutionDAO) : JFrame() {
    private inner class ScramblePanel(scrambleManager: ScrambleManager) : JPanel() {
        private var scrambleViewerPanel: ScrambleViewerPanel? = null
        fun setScrambleViewerPanel(scrambleViewerPanel: ScrambleViewerPanel?) {
            this.scrambleViewerPanel = scrambleViewerPanel
        }

        private fun createComponents() {
            layout = WrapLayout(10, 3)
        }

        private fun setScramble(scramble: Scramble?) {
            removeAll()
            val labels = arrayOfNulls<JLabel>(scramble!!.sequence.length)
            for (i in labels.indices) {
                labels[i] = JLabel(scramble!!.sequence[i])
                labels[i]!!.font = Font("Arial", Font.PLAIN, 18)
                labels[i]!!.cursor = Cursor(Cursor.HAND_CURSOR)
                val index = i
                labels[i]!!.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        for (i in labels.indices) {
                            labels[i]!!.foreground = if (i <= index) Color.BLACK else Color.LIGHT_GRAY
                        }
                        this@ScramblePanel.scrambleViewerPanel!!.setScramble(
                                Scramble(
                                        scramble.scramblerId,
                                        Arrays.copyOf(scramble.sequence, index + 1)))
                    }
                })
                add(labels[i], "gap 10")
            }
            revalidate()
            repaint()
        }

        init {
            createComponents()
            scrambleManager.addListener(object : ScrambleManager.Listener() {
                override fun scrambleChanged(scramble: Scramble?) {
                    setScramble(scramble)
                }
            })
        }
    }

    private inner class TimerPanel(timerManager: TimerManager) : JPanel() {
        private var leftHand: HandImage? = null
        private var timeLabel: TimeLabel? = null
        var textFieldTime: JTextField? = null
        private var rightHand: HandImage? = null
        private var currentManualInput = false
        private var isInspectionRunning = false
        private var time: Long = 0
        private fun createComponents() {
            layout = MigLayout("fill", "2%[19%]1%[56%]1%[19%]2%")

            // leftHand
            leftHand = HandImage(false)
            add(leftHand, "grow")

            // timeLabel
            timeLabel = TimeLabel(formatMinutes(0, configurationManager.getConfiguration("TIMER-PRECISION")!!, false))
            timeLabel!!.font = Font("Arial", Font.BOLD, 108)
            add(timeLabel, "grow")

            // textFieldTime
            textFieldTime = JTextField()
            textFieldTime!!.horizontalAlignment = JTextField.CENTER
            textFieldTime!!.font = Font("Arial", Font.BOLD, 108)

            // rightHand
            rightHand = HandImage(true)
            add(rightHand, "grow")
            currentManualInput = false
            updateTimer(configurationManager.getConfiguration("TIMER-TRIGGER") == "MANUAL-INPUT")
        }

        fun updateTimer(manualInput: Boolean) {
            if (currentManualInput && !manualInput) {
                remove(textFieldTime)
                layout = MigLayout("fill", "2%[19%]1%[56%]1%[19%]2%")
                add(leftHand, "grow")
                add(timeLabel, "grow")
                add(rightHand, "grow")
                this@MainFrame.requestFocusInWindow()
                updateUI()
                currentManualInput = false
            } else if (!currentManualInput && manualInput) {
                remove(timeLabel)
                remove(leftHand)
                remove(rightHand)
                layout = MigLayout("fill", "12%[76%]12%")
                add(textFieldTime, "growx")
                textFieldTime!!.requestFocusInWindow()
                updateUI()
                currentManualInput = true
            }
        }

        init {
            createComponents()
            timerManager.addListener(object : TimerManager.Listener() {
                override fun timerReset() {
                    isInspectionRunning = false
                    time = 0
                    timeLabel!!.foreground = Color.BLACK
                    timeLabel!!.setText(
                            formatMinutes(time, configurationManager.getConfiguration("TIMER-PRECISION")!!, false))
                }

                override fun leftHandPressed() {
                    leftHand!!.setPressed(true)
                }

                override fun leftHandReleased() {
                    leftHand!!.setPressed(false)
                }

                override fun rightHandPressed() {
                    rightHand!!.setPressed(true)
                }

                override fun rightHandReleased() {
                    rightHand!!.setPressed(false)
                }

                override fun inspectionRunning(remainingTime: Long) {
                    var remainingTime = remainingTime
                    isInspectionRunning = true
                    val startColor = Color.BLACK
                    val endColor = Color(0xD4, 0x11, 0x11)
                    val color: Color
                    if (remainingTime > 7000) {
                        color = startColor
                    } else if (remainingTime > 0) {
                        val x = remainingTime / 7000.0
                        color = Color(
                                (x * startColor.red + (1 - x) * endColor.red).toInt(),
                                (x * startColor.green + (1 - x) * endColor.green).toInt(),
                                (x * startColor.blue + (1 - x) * endColor.blue).toInt())
                    } else {
                        color = endColor
                        remainingTime = 0
                    }
                    timeLabel!!.foreground = color
                    timeLabel!!.setText(
                            java.lang.Long.toString(Math.ceil(remainingTime / 1000.0).toLong()))
                }

                override fun solutionRunning(timing: Timing?, hidden: Boolean) {
                    isInspectionRunning = false
                    time = timing!!.elapsedTime
                    timeLabel!!.foreground = Color.BLACK
                    timeLabel!!.setText(
                            formatMinutes(time, configurationManager.getConfiguration("TIMER-PRECISION")!!, false))
                    if (hidden) timeLabel!!.isVisible = false
                }

                override fun solutionStarted() {
                    scramblePanel!!.isVisible = true
                }

                override fun solutionFinished(timing: Timing?, penalty: String?) {
                    isInspectionRunning = false
                    time = timing!!.elapsedTime
                    timeLabel!!.foreground = Color.BLACK
                    timeLabel!!.setText(
                            formatMinutes(time, configurationManager.getConfiguration("TIMER-PRECISION")!!, false))
                    timeLabel!!.isVisible = true
                    scramblePanel!!.isVisible = true
                }

                override fun precisionChanged(timerPrecisionId: String?) {
                    timeLabel!!.setTimerPrecision(timerPrecisionId)
                    if (!isInspectionRunning) {
                        timeLabel!!.foreground = Color.BLACK
                        timeLabel!!.setText(formatMinutes(time, configurationManager.getConfiguration("TIMER-PRECISION")!!, false))
                    }
                }
            })
        }
    }

    private inner class TimesScrollPane(private val solutionManager: SolutionManager, sessionManager: SessionManager) : JScrollPane() {
        private var panel: JPanel? = null
        private fun createComponents() {
            // scroll doesn't work without this
            preferredSize = Dimension(0, 0)

            // panel
            panel = JPanel(
                    MigLayout(
                            "center",
                            "0[right]8[pref!]16[pref!]8[pref!]16[pref!]8[pref!]8[pref!]0",
                            ""))
        }

        private fun setSolutions(solutions: Array<Solution?>?) {
            panel!!.removeAll()
            for (i in solutions!!.indices) {
                val solution = solutions[i]
                val labelIndex = JLabel(Integer.toString(solutions.size - i) + ".")
                labelIndex.font = Font("Tahoma", Font.BOLD, 13)
                panel!!.add(labelIndex)
                val labelTime = JLabel(formatMinutes(solutions[i]!!.timing.elapsedTime, configurationManager.getConfiguration("TIMER-PRECISION")!!, false))
                labelTime.font = Font("Tahoma", Font.PLAIN, 13)
                panel!!.add(labelTime)
                val labelPlus2 = JLabel("+2")
                labelPlus2.font = Font("Tahoma", Font.PLAIN, 13)
                if (solution!!.penalty != "+2") {
                    labelPlus2.foreground = Color.LIGHT_GRAY
                }
                labelPlus2.cursor = Cursor(Cursor.HAND_CURSOR)
                labelPlus2.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        if (solution.penalty != "+2") {
                            this@TimesScrollPane.solutionManager.updateSolution(
                                    solution.setPenalty("+2"))
                        } else if (solution.penalty == "+2") {
                            this@TimesScrollPane.solutionManager.updateSolution(
                                    solution.setPenalty(""))
                        }
                    }
                })
                panel!!.add(labelPlus2)
                val labelDNF = JLabel("DNF")
                labelDNF.font = Font("Tahoma", Font.PLAIN, 13)
                if (solution.penalty != "DNF") {
                    labelDNF.foreground = Color.LIGHT_GRAY
                }
                labelDNF.cursor = Cursor(Cursor.HAND_CURSOR)
                labelDNF.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        if (solution.penalty != "DNF") {
                            this@TimesScrollPane.solutionManager.updateSolution(
                                    solution.setPenalty("DNF"))
                        } else if (solution.penalty == "DNF") {
                            this@TimesScrollPane.solutionManager.updateSolution(
                                    solution.setPenalty(""))
                        }
                    }
                })
                panel!!.add(labelDNF)
                val labelRetry = JLabel()
                labelRetry.icon = ImageIcon(javaClass.getResource("/com/puzzletimer/resources/retry.png"))
                labelRetry.cursor = Cursor(Cursor.HAND_CURSOR)
                labelRetry.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        scrambleManager.addScrambles(arrayOf(solution.scramble), true)
                        this@TimesScrollPane.solutionManager.removeSolution(solution)
                        scrambleManager.changeScramble()
                    }
                })
                panel!!.add(labelRetry)
                val labelEdit = JLabel()
                labelEdit.icon = ImageIcon(javaClass.getResource("/com/puzzletimer/resources/edit.png"))
                labelEdit.cursor = Cursor(Cursor.HAND_CURSOR)
                labelEdit.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        val listener: SolutionEditingDialogListener = object : SolutionEditingDialogListener() {
                            override fun solutionEdited(solution: Solution?) {
                                solutionManager.updateSolution(solution!!)

                                // check for personal records
                                val measures = arrayOf(
                                        Best(1, Int.MAX_VALUE),
                                        BestMean(3, 3),
                                        BestMean(100, 100),
                                        BestAverage(5, 5),
                                        BestAverage(12, 12),
                                        BestAverage(50, 50))
                                val descriptions = arrayOf<String>(
                                        _("main.single"),
                                        _("main.mean_of_3"),
                                        _("main.mean_of_100"),
                                        _("main.average_of_5"),
                                        _("main.average_of_12"),
                                        _("main.average_of_50"))
                                val solutions = this@MainFrame.solutionManager.getSolutions()
                                val sessionSolutions = sessionManager.getSolutions()
                                for (i in measures.indices) {
                                    if (sessionSolutions.size < measures[i].minimumWindowSize) {
                                        continue
                                    }
                                    measures[i].setSolutions(solutions, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
                                    val allTimeBest = measures[i].value
                                    measures[i].setSolutions(sessionSolutions, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
                                    val sessionBest = measures[i].value
                                    if (measures[i].windowPosition == 0 && sessionBest <= allTimeBest) {
                                        messageManager.enqueueMessage(
                                                MessageManager.MessageType.INFORMATION,
                                                java.lang.String.format(_("main.personal_record_message"),
                                                        categoryManager.getCurrentCategory().getDescription(),
                                                        formatMinutes(measures[i].value, configurationManager.getConfiguration("TIMER-PRECISION")!!, measures[i].round),
                                                        descriptions[i]))
                                    }
                                }
                            }
                        }
                        val solutionEditingDialog = SolutionEditingDialog(
                                this@MainFrame,
                                true,
                                solution,
                                listener,
                                configurationManager)
                        solutionEditingDialog.setLocationRelativeTo(null)
                        solutionEditingDialog.isVisible = true
                    }
                })
                ToolTipManager.sharedInstance().initialDelay = 0
                ToolTipManager.sharedInstance().dismissDelay = 10000
                if (!solution.comment.isEmpty()) labelEdit.toolTipText = solution.comment
                panel!!.add(labelEdit)
                val labelX = JLabel()
                labelX.icon = ImageIcon(javaClass.getResource("/com/puzzletimer/resources/x.png"))
                labelX.cursor = Cursor(Cursor.HAND_CURSOR)
                labelX.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        this@TimesScrollPane.solutionManager.removeSolution(solution)
                    }
                })
                panel!!.add(labelX, "wrap")
            }
            setViewportView(panel)
        }

        init {
            createComponents()
            timerManager.addListener(object : TimerManager.Listener() {
                override fun precisionChanged(timerPrecisionId: String?) {
                    val solutions = this@MainFrame.sessionManager.getSolutions()
                    setSolutions(solutions)
                }
            })
            sessionManager.addListener(object : SessionManager.Listener() {
                override fun solutionsUpdated(solutions: Array<Solution?>?) {
                    setSolutions(solutions)
                }
            })
        }
    }

    private inner class StatisticsPanel(sessionManager: SessionManager) : JPanel() {
        private var labelMean: JLabel? = null
        private var labelAverage: JLabel? = null
        private var labelBestTime: JLabel? = null
        private var labelMedian: JLabel? = null
        private var labelWorstTime: JLabel? = null
        private var labelStandardDeviation: JLabel? = null
        private var labelMeanOf3: JLabel? = null
        private var labelBestMeanOf3: JLabel? = null
        private var labelAverageOf5: JLabel? = null
        private var labelBestAverageOf5: JLabel? = null
        private var labelAverageOf12: JLabel? = null
        private var labelBestAverageOf12: JLabel? = null
        private var labelAverageOf50: JLabel? = null
        private var labelBestAverageOf50: JLabel? = null
        private var nullTime = "XX:XX.XX"
        private fun createComponents() {
            layout = MigLayout(
                    "center",
                    "[pref!,right]8[pref!]",
                    "1[pref!]1[pref!]1[pref!]1[pref!]1[pref!]1[pref!]6[pref!]1[pref!]6[pref!]1[pref!]6[pref!]1[pref!]6[pref!]1[pref!]1")

            // labelMean
            val labelMeanDescription = JLabel(_("statistics.mean"))
            labelMeanDescription.font = Font("Tahoma", Font.BOLD, 11)
            add(labelMeanDescription)
            labelMean = JLabel(nullTime)
            add(labelMean, "wrap")

            // labelAverage
            val labelAverageDescription = JLabel(_("statistics.average"))
            labelAverageDescription.font = Font("Tahoma", Font.BOLD, 11)
            add(labelAverageDescription)
            labelAverage = JLabel(nullTime)
            add(labelAverage, "wrap")

            // labelBestTime
            val labelBestTimeDescription = JLabel(_("statistics.best_time"))
            labelBestTimeDescription.font = Font("Tahoma", Font.BOLD, 11)
            add(labelBestTimeDescription)
            labelBestTime = JLabel(nullTime)
            add(labelBestTime, "wrap")

            // labelMedian
            val labelMedianDescription = JLabel(_("statistics.median"))
            labelMedianDescription.font = Font("Tahoma", Font.BOLD, 11)
            add(labelMedianDescription)
            labelMedian = JLabel(nullTime)
            add(labelMedian, "wrap")

            // labelWorstTime
            val labelWorstTimeDescription = JLabel(_("statistics.worst_time"))
            labelWorstTimeDescription.font = Font("Tahoma", Font.BOLD, 11)
            add(labelWorstTimeDescription)
            labelWorstTime = JLabel(nullTime)
            add(labelWorstTime, "wrap")

            // labelStandardDeviation
            val labelStandardDeviationDescription = JLabel(_("statistics.standard_deviation"))
            labelStandardDeviationDescription.font = Font("Tahoma", Font.BOLD, 11)
            add(labelStandardDeviationDescription)
            labelStandardDeviation = JLabel(nullTime)
            add(labelStandardDeviation, "wrap")

            // labelMeanOf3
            val labelMeanOf3Description = JLabel(_("statistics.mean_of_3"))
            labelMeanOf3Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelMeanOf3Description)
            labelMeanOf3 = JLabel(nullTime)
            add(labelMeanOf3, "wrap")

            // labelBestMeanOf3
            val labelBestMeanOf3Description = JLabel(_("statistics.best_mean_of_3"))
            labelBestMeanOf3Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelBestMeanOf3Description)
            labelBestMeanOf3 = JLabel(nullTime)
            add(labelBestMeanOf3, "wrap")

            // labelAverageOf5
            val labelAverageOf5Description = JLabel(_("statistics.average_of_5"))
            labelAverageOf5Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelAverageOf5Description)
            labelAverageOf5 = JLabel(nullTime)
            add(labelAverageOf5, "wrap")

            // labelBestAverageOf5
            val labelBestAverageOf5Description = JLabel(_("statistics.best_average_of_5"))
            labelBestAverageOf5Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelBestAverageOf5Description)
            labelBestAverageOf5 = JLabel(nullTime)
            add(labelBestAverageOf5, "wrap")

            // labelAverageOf12
            val labelAverageOf12Description = JLabel(_("statistics.average_of_12"))
            labelAverageOf12Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelAverageOf12Description)
            labelAverageOf12 = JLabel(nullTime)
            add(labelAverageOf12, "wrap")

            // labelBestAverageOf12
            val labelBestAverageOf12Description = JLabel(_("statistics.best_average_of_12"))
            labelBestAverageOf12Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelBestAverageOf12Description)
            labelBestAverageOf12 = JLabel(nullTime)
            add(labelBestAverageOf12, "wrap")

            // labelAverageOf50
            val labelAverageOf50Description = JLabel(_("statistics.average_of_50"))
            labelAverageOf50Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelAverageOf50Description)
            labelAverageOf50 = JLabel(nullTime)
            add(labelAverageOf50, "wrap")

            // labelBestAverageOf50
            val labelBestAverageOf50Description = JLabel(_("statistics.best_average_of_50"))
            labelBestAverageOf50Description.font = Font("Tahoma", Font.BOLD, 11)
            add(labelBestAverageOf50Description)
            labelBestAverageOf50 = JLabel(nullTime)
            add(labelBestAverageOf50, "wrap")
        }

        init {
            createComponents()
            val labels = arrayOf(
                    labelMean,
                    labelAverage,
                    labelBestTime,
                    labelMedian,
                    labelWorstTime,
                    labelStandardDeviation,
                    labelMeanOf3,
                    labelBestMeanOf3,
                    labelAverageOf5,
                    labelBestAverageOf5,
                    labelAverageOf12,
                    labelBestAverageOf12,
                    labelAverageOf50,
                    labelBestAverageOf50)
            val measures = arrayOf(
                    Mean(1, Int.MAX_VALUE),
                    Average(3, Int.MAX_VALUE),
                    Best(1, Int.MAX_VALUE),
                    Percentile(1, Int.MAX_VALUE, 0.5),
                    Worst(1, Int.MAX_VALUE),
                    StandardDeviation(1, Int.MAX_VALUE),
                    Mean(3, 3),
                    BestMean(3, Int.MAX_VALUE),
                    Average(5, 5),
                    BestAverage(5, Int.MAX_VALUE),
                    Average(12, 12),
                    BestAverage(12, Int.MAX_VALUE),
                    Average(50, 50),
                    BestAverage(50, Int.MAX_VALUE))
            timerManager.addListener(object : TimerManager.Listener() {
                override fun precisionChanged(timerPrecisionId: String?) {
                    val solutions = this@MainFrame.sessionManager.getSolutions()
                    if (timerPrecisionId == "CENTISECONDS") {
                        nullTime = "XX:XX.XX"
                    } else if (timerPrecisionId == "MILLISECONDS") {
                        nullTime = "XX:XX.XXX"
                    }
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
                    }
                }
            })
            sessionManager.addListener(object : SessionManager.Listener() {
                override fun solutionsUpdated(solutions: Array<Solution?>?) {
                    for (i in labels.indices) {
                        if (solutions!!.size >= measures[i].minimumWindowSize) {
                            val size = Math.min(solutions.size, measures[i].maximumWindowSize)
                            val window = arrayOfNulls<Solution>(size)
                            System.arraycopy(solutions, 0, window, 0, size)
                            measures[i].setSolutions(window, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
                            labels[i]!!.text = formatMinutes(measures[i].value, configurationManager.getConfiguration("TIMER-PRECISION")!!, measures[i].round)
                        } else {
                            labels[i]!!.text = nullTime
                        }
                    }
                }
            })
        }
    }

    private inner class ScrambleViewerPanel(
            private val puzzleProvider: PuzzleProvider,
            private val colorManager: ColorManager,
            private val scramblerProvider: ScramblerProvider,
            scrambleManager: ScrambleManager) : JPanel() {
        private var hide: JCheckBox? = null
        private var panel3D: Panel3D? = null
        private fun createComponents() {
            layout = MigLayout("fill", "0[fill]0", "0[pref!]0[fill]0")

            // panel 3d
            panel3D = Panel3D()
            panel3D!!.isFocusable = false
            hide = JCheckBox(_("main.hide"))
            hide!!.isFocusable = false
            add(hide, "wrap")
            add(panel3D)
        }

        fun setScramble(scramble: Scramble?) {
            val scrambler = this.scramblerProvider[scramble!!.scramblerId]
            val puzzle = this.puzzleProvider[scrambler!!.scramblerInfo.puzzleId]
            val colorScheme = this.colorManager.getColorScheme(puzzle!!.puzzleInfo.puzzleId)
            panel3D!!.setMesh(puzzle.getScrambledPuzzleMesh(colorScheme!!, scramble.sequence))
        }

        init {
            createComponents()
            scrambleManager.addListener(object : ScrambleManager.Listener() {
                override fun scrambleChanged(scramble: Scramble?) {
                    setScramble(scramble)
                }
            })
            hide!!.addActionListener { e: ActionEvent? ->
                if (hide!!.isSelected) {
                    panel3D!!.isVisible = false
                } else {
                    panel3D!!.isVisible = true
                }
            }
        }
    }

    private var menuFile: JMenu? = null
    private var menuItemAddSolution: JMenuItem? = null
    private var menuItemExit: JMenuItem? = null
    private var menuItemTips: JMenuItem? = null
    private var menuItemScrambleQueue: JMenuItem? = null
    private var menuItemHistory: JMenuItem? = null
    private var menuItemSessionSummary: JMenuItem? = null
    private var menuItemStackmatDeveloper: JMenuItem? = null
    private var menuCategory: JMenu? = null
    private var menuItemColorScheme: JMenuItem? = null
    private var menuItemInspectionTime: JCheckBoxMenuItem? = null
    private var menuItemAnyKey: JCheckBoxMenuItem? = null
    private var menuItemHideTimer: JCheckBoxMenuItem? = null
    private var menuItemSmoothTiming: JCheckBoxMenuItem? = null
    private var stackmatTimerInputDevice: JMenu? = null
    private var stackmatTimerInputDeviceGroup: ButtonGroup? = null
    private var menuDailySession: JCheckBoxMenuItem? = null
    private var menuItemManualInput: JRadioButtonMenuItem? = null
    private var menuItemCtrlKeys: JRadioButtonMenuItem? = null
    private var menuItemSpaceKey: JRadioButtonMenuItem? = null
    private var menuItemStackmatTimer: JRadioButtonMenuItem? = null
    private var menuItemCentiseconds: JRadioButtonMenuItem? = null
    private var menuItemMilliseconds: JRadioButtonMenuItem? = null
    private var menuLookAndFeel: JMenu? = null
    private var lookAndFeelGroup: ButtonGroup? = null
    private var menuItemDefaultLnF: JRadioButtonMenuItem? = null
    private var menuItemAbout: JMenuItem? = null
    private var menuItemFeedback: JMenuItem? = null
    private var labelMessage: JLabel? = null
    private var scramblePanel: ScramblePanel? = null
    private var timerPanel: TimerPanel? = null
    private var timesScrollPane: TimesScrollPane? = null
    private var statisticsPanel: StatisticsPanel? = null
    private var scrambleViewerPanel: ScrambleViewerPanel? = null
    private var tipsFrame: TipsFrame? = null
    private var scrambleQueueFrame: ScrambleQueueFrame? = null
    private var historyFrame: HistoryFrame? = null
    private var sessionSummaryFrame: SessionSummaryFrame? = null
    private var stackmatDeveloperFrame: StackmatDeveloperFrame? = null
    private var categoryManagerDialog: CategoryManagerFrame? = null
    private var colorSchemeFrame: ColorSchemeFrame? = null
    private val audioFormat: AudioFormat
    private val mixerInfo: Mixer.Info?
    private var toolsMenu: JMenu? = null
    private var exportSolutionsToCSV: JMenuItem? = null
    var update = false
    fun hasUpdate(): Boolean {
        return update
    }

    private fun changeLookAndFeel(className: String, mainFrame: MainFrame) {
        configurationManager.setConfiguration("LOOK-AND-FEEL", className)
        try {
            UIManager.setLookAndFeel(className)
        } catch (e: Exception) {
        }
        SwingUtilities.updateComponentTreeUI(this)
        SwingUtilities.updateComponentTreeUI(tipsFrame)
        SwingUtilities.updateComponentTreeUI(scrambleQueueFrame)
        SwingUtilities.updateComponentTreeUI(historyFrame)
        SwingUtilities.updateComponentTreeUI(sessionSummaryFrame)
        SwingUtilities.updateComponentTreeUI(stackmatDeveloperFrame)
        SwingUtilities.updateComponentTreeUI(categoryManagerDialog)
        SwingUtilities.updateComponentTreeUI(colorSchemeFrame)
        pack()
        tipsFrame!!.pack()
        scrambleQueueFrame!!.pack()
        historyFrame!!.pack()
        sessionSummaryFrame!!.pack()
        stackmatDeveloperFrame!!.pack()
        categoryManagerDialog!!.pack()
        colorSchemeFrame!!.pack()
    }

    private fun setTimerTrigger(timerTriggerId: String?) {
        if (timerTriggerId == "MANUAL-INPUT") {
            menuItemManualInput!!.isSelected = true
            timerPanel!!.updateTimer(true)
            timerManager.setTimer(
                    ManualInputTimer(timerManager, timerPanel!!.textFieldTime!!))
        } else if (timerTriggerId == "KEYBOARD-TIMER-CONTROL") {
            menuItemCtrlKeys!!.isSelected = true
            timerPanel!!.updateTimer(false)
            timerManager.setTimer(
                    ControlKeysTimer(this, timerManager))
        } else if (timerTriggerId == "KEYBOARD-TIMER-SPACE") {
            menuItemSpaceKey!!.isSelected = true
            timerPanel!!.updateTimer(false)
            timerManager.setTimer(
                    SpaceKeyTimer(this, timerManager))
        } else if (timerTriggerId == "STACKMAT-TIMER") {
            if (mixerInfo != null) {
                var targetDataLine: TargetDataLine? = null
                try {
                    targetDataLine = AudioSystem.getTargetDataLine(audioFormat, mixerInfo)
                    targetDataLine.open(audioFormat)
                    menuItemStackmatTimer!!.isSelected = true
                    timerPanel!!.updateTimer(false)
                    timerManager.setTimer(
                            StackmatTimer(targetDataLine, timerManager, stackmatDeveloperFrame))
                } catch (e: LineUnavailableException) {
                    // select the default timer
                    menuItemSpaceKey!!.isSelected = true
                    timerPanel!!.updateTimer(false)
                    timerManager.setTimer(
                            SpaceKeyTimer(this, timerManager))
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR,
                            _("main.stackmat_timer_error_message"))
                }
            } else {
                // select the default timer
                menuItemSpaceKey!!.isSelected = true
                timerPanel!!.updateTimer(false)
                timerManager.setTimer(
                        SpaceKeyTimer(this, timerManager))
                messageManager.enqueueMessage(
                        MessageManager.MessageType.ERROR,
                        _("main.stackmat_timer_error_message"))
            }
        }
    }

    private fun setTimerPrecision(timerPrecisionId: String?) {
        if (timerPrecisionId == "CENTISECONDS") {
            menuItemCentiseconds!!.isSelected = true
        } else if (timerPrecisionId == "MILLISECONDS") {
            menuItemMilliseconds!!.isSelected = true
        }
        timerManager.setPrecision(timerPrecisionId)
    }

    private fun createComponents() {
        val menuShortcutKey = Toolkit.getDefaultToolkit().menuShortcutKeyMask

        // menuBar
        val menuBar = JMenuBar()
        jMenuBar = menuBar

        // menuFile
        menuFile = JMenu(_("main.file"))
        menuFile!!.mnemonic = KeyEvent.VK_F
        menuBar.add(menuFile)

        // menuItemAddSolution
        menuItemAddSolution = JMenuItem(_("main.add_solution"))
        menuItemAddSolution!!.mnemonic = KeyEvent.VK_A
        menuItemAddSolution!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_A, menuShortcutKey)
        menuFile!!.add(menuItemAddSolution)
        menuFile!!.addSeparator()

        // menuItemExit
        menuItemExit = JMenuItem(_("main.exit"))
        menuItemExit!!.mnemonic = KeyEvent.VK_X
        menuFile!!.add(menuItemExit)

        // menuView
        val menuView = JMenu(_("main.view"))
        menuView.mnemonic = KeyEvent.VK_V
        menuBar.add(menuView)

        // menuItemTips
        menuItemTips = JMenuItem(_("main.tips"))
        menuItemTips!!.mnemonic = KeyEvent.VK_T
        menuItemTips!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, menuShortcutKey or KeyEvent.ALT_MASK)
        menuView.add(menuItemTips)

        // menuItemScrambleQueue
        menuItemScrambleQueue = JMenuItem(_("main.scramble_queue"))
        menuItemScrambleQueue!!.mnemonic = KeyEvent.VK_Q
        menuItemScrambleQueue!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, menuShortcutKey or KeyEvent.ALT_MASK)
        menuView.add(menuItemScrambleQueue)

        // menuItemHistory
        menuItemHistory = JMenuItem(_("main.history"))
        menuItemHistory!!.mnemonic = KeyEvent.VK_H
        menuItemHistory!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_H, menuShortcutKey or KeyEvent.ALT_MASK)
        menuView.add(menuItemHistory)

        // menuItemSessionSummary
        menuItemSessionSummary = JMenuItem(_("main.session_summary"))
        menuItemSessionSummary!!.mnemonic = KeyEvent.VK_S
        menuItemSessionSummary!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcutKey or KeyEvent.ALT_MASK)
        menuView.add(menuItemSessionSummary)

        // menuItemStackmatDeveloper
        menuItemStackmatDeveloper = JMenuItem(_("main.stackmat_developer"))
        menuItemStackmatDeveloper!!.mnemonic = KeyEvent.VK_E
        menuItemStackmatDeveloper!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_E, menuShortcutKey or KeyEvent.ALT_MASK)
        menuView.add(menuItemStackmatDeveloper)

        // menuCategory
        menuCategory = JMenu(_("main.category"))
        menuCategory!!.mnemonic = KeyEvent.VK_C
        menuBar.add(menuCategory)

        // menuOptions
        val menuOptions = JMenu(_("main.options"))
        menuOptions.mnemonic = KeyEvent.VK_O
        menuBar.add(menuOptions)

        // menuColorScheme
        menuItemColorScheme = JMenuItem(_("main.color_scheme"))
        menuItemColorScheme!!.mnemonic = KeyEvent.VK_C
        menuItemColorScheme!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_K, menuShortcutKey or KeyEvent.ALT_MASK)
        menuOptions.add(menuItemColorScheme)

        // menuItemInspectionTime
        menuItemInspectionTime = JCheckBoxMenuItem(_("main.inspection_time"))
        menuItemInspectionTime!!.mnemonic = KeyEvent.VK_I
        menuItemInspectionTime!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_I, menuShortcutKey or KeyEvent.ALT_MASK)
        menuOptions.add(menuItemInspectionTime)

        //menuItemAnyKey
        menuItemAnyKey = JCheckBoxMenuItem(_("main.any_key"))
        menuItemAnyKey!!.mnemonic = KeyEvent.VK_A
        menuItemAnyKey!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_A, menuShortcutKey or KeyEvent.ALT_MASK)
        menuOptions.add(menuItemAnyKey)

        //menuItemHideTimer
        menuItemHideTimer = JCheckBoxMenuItem(_("main.hide_timer"))
        menuOptions.add(menuItemHideTimer)

        //menuDailySession
        menuDailySession = JCheckBoxMenuItem(_("main.daily_session"))
        menuOptions.add(menuDailySession)

        // menuItemSmoothTiming
        menuItemSmoothTiming = JCheckBoxMenuItem(_("main.smooth_timing"))
        menuItemSmoothTiming!!.mnemonic = KeyEvent.VK_M
        menuItemSmoothTiming!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_M, menuShortcutKey or KeyEvent.ALT_MASK)
        menuOptions.add(menuItemSmoothTiming)

        // menuTimerTrigger
        val menuTimerTrigger = JMenu(_("main.timer_trigger"))
        menuTimerTrigger.mnemonic = KeyEvent.VK_T
        menuOptions.add(menuTimerTrigger)
        val timerTriggerGroup = ButtonGroup()

        // menuItemManualInput
        menuItemManualInput = JRadioButtonMenuItem(_("main.manual_input"))
        menuItemManualInput!!.mnemonic = KeyEvent.VK_N
        menuItemManualInput!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, menuShortcutKey)
        menuTimerTrigger.add(menuItemManualInput)
        timerTriggerGroup.add(menuItemManualInput)

        // menuItemCtrlKeys
        menuItemCtrlKeys = JRadioButtonMenuItem(_("main.ctrl_keys"))
        menuItemCtrlKeys!!.mnemonic = KeyEvent.VK_C
        menuItemCtrlKeys!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, menuShortcutKey)
        menuTimerTrigger.add(menuItemCtrlKeys)
        timerTriggerGroup.add(menuItemCtrlKeys)

        // menuItemSpaceKey
        menuItemSpaceKey = JRadioButtonMenuItem(_("main.space_key"))
        menuItemSpaceKey!!.mnemonic = KeyEvent.VK_S
        menuItemSpaceKey!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, menuShortcutKey)
        menuTimerTrigger.add(menuItemSpaceKey)
        timerTriggerGroup.add(menuItemSpaceKey)

        // menuItemStackmatTimer
        menuItemStackmatTimer = JRadioButtonMenuItem(_("main.stackmat_timer"))
        menuItemStackmatTimer!!.mnemonic = KeyEvent.VK_T
        menuItemStackmatTimer!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, menuShortcutKey)
        menuTimerTrigger.add(menuItemStackmatTimer)
        timerTriggerGroup.add(menuItemStackmatTimer)

        // menuStackmatTimerInputDevice
        stackmatTimerInputDevice = JMenu(_("main.stackmat_timer_input_device"))
        menuTimerTrigger.mnemonic = KeyEvent.VK_S
        menuOptions.add(stackmatTimerInputDevice)
        stackmatTimerInputDeviceGroup = ButtonGroup()

        // menuLookAndFeel
        menuLookAndFeel = JMenu(_("main.look_and_feel"))
        menuLookAndFeel!!.mnemonic = KeyEvent.VK_L
        menuOptions.add(menuLookAndFeel)
        lookAndFeelGroup = ButtonGroup()

        // menuItemDefaultLaF
        menuItemDefaultLnF = JRadioButtonMenuItem(_("main.laf_system_default"))
        menuItemDefaultLnF!!.mnemonic = KeyEvent.VK_D
        menuItemDefaultLnF!!.isSelected = true
        menuLookAndFeel!!.add(menuItemDefaultLnF)
        lookAndFeelGroup!!.add(menuItemDefaultLnF)

        // menuTimerPrecision
        val menuTimerPrecision = JMenu(_("main.timer_precision"))
        menuTimerPrecision.mnemonic = KeyEvent.VK_P
        menuOptions.add(menuTimerPrecision)
        val timerPrecisionGroup = ButtonGroup()

        // menuItemCentiseconds
        menuItemCentiseconds = JRadioButtonMenuItem(_("main.centiseconds"))
        menuItemCentiseconds!!.mnemonic = KeyEvent.VK_C
        menuItemCentiseconds!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, menuShortcutKey)
        menuTimerPrecision.add(menuItemCentiseconds)
        timerPrecisionGroup.add(menuItemCentiseconds)

        // menuItemMilliseconds
        menuItemMilliseconds = JRadioButtonMenuItem(_("main.milliseconds"))
        menuItemMilliseconds!!.mnemonic = KeyEvent.VK_M
        menuItemMilliseconds!!.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_M, menuShortcutKey)
        menuTimerPrecision.add(menuItemMilliseconds)
        timerPrecisionGroup.add(menuItemMilliseconds)

        //menuTools
        toolsMenu = JMenu(_("main.tools"))
        exportSolutionsToCSV = JMenuItem(_("main.export_csv"))
        toolsMenu!!.add(exportSolutionsToCSV)
        menuBar.add(toolsMenu)

        //menuHelp
        val menuHelp = JMenu(_("main.help"))
        menuHelp.mnemonic = KeyEvent.VK_H
        menuBar.add(menuHelp)

        // menuItemFeedback
        menuItemFeedback = JMenuItem(_("main.feedback"))
        menuItemFeedback!!.mnemonic = KeyEvent.VK_F
        menuHelp.add(menuItemFeedback)

        // menuItemAbout
        menuItemAbout = JMenuItem(_("main.about"))
        menuItemAbout!!.mnemonic = KeyEvent.VK_A
        menuHelp.add(menuItemAbout)

        // panelMain
        val panelMain = JPanel(
                MigLayout(
                        "fill, hidemode 1, insets 2 3 2 3",
                        "[fill]",
                        "[pref!][pref!][fill, growprio 200][pref!]"))
        panelMain.preferredSize = Dimension(0, 0)
        add(panelMain)

        // labelMessage
        labelMessage = JLabel()
        labelMessage!!.preferredSize = Dimension()
        labelMessage!!.isOpaque = true
        labelMessage!!.horizontalAlignment = JLabel.CENTER
        labelMessage!!.foreground = Color(0xFF, 0xFF, 0xFF)
        labelMessage!!.isVisible = false
        panelMain.add(labelMessage, "wrap")

        // panelScramble
        scramblePanel = ScramblePanel(scrambleManager)
        panelMain.add(scramblePanel, "wrap")

        // timer panel
        timerPanel = TimerPanel(timerManager)
        panelMain.add(timerPanel, "wrap")

        // statistics panel
        statisticsPanel = StatisticsPanel(sessionManager)
        statisticsPanel!!.border = BorderFactory.createTitledBorder(_("main.session_statistics"))
        panelMain.add(statisticsPanel, "w 30%, growy, gapright 0, split 3")

        // times scroll pane
        timesScrollPane = TimesScrollPane(solutionManager, sessionManager)
        timesScrollPane!!.border = BorderFactory.createTitledBorder(_("main.times"))
        timesScrollPane!!.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        panelMain.add(timesScrollPane, "w 40%, growy, gapright 0")

        // scramble viewer panel
        scrambleViewerPanel = ScrambleViewerPanel(
                puzzleProvider,
                colorManager,
                scramblerProvider,
                scrambleManager)
        scrambleViewerPanel!!.border = BorderFactory.createTitledBorder(_("main.scramble"))
        panelMain.add(scrambleViewerPanel, "w 30%, growy")
        scramblePanel!!.setScrambleViewerPanel(scrambleViewerPanel)
        val icon = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/com/puzzletimer/resources/icon.png"))

        // tips frame
        tipsFrame = TipsFrame(
                puzzleProvider,
                tipProvider,
                scramblerProvider,
                categoryManager,
                scrambleManager)
        tipsFrame!!.setLocationRelativeTo(null)
        tipsFrame!!.iconImage = icon

        // scramble queue frame
        scrambleQueueFrame = ScrambleQueueFrame(
                scrambleParserProvider,
                scramblerProvider,
                categoryManager,
                scrambleManager)
        scrambleQueueFrame!!.setLocationRelativeTo(null)
        scrambleQueueFrame!!.iconImage = icon

        // history frame
        historyFrame = HistoryFrame(
                scramblerProvider,
                scrambleParserProvider,
                categoryManager,
                scrambleManager,
                solutionManager,
                sessionManager,
                timerManager,
                configurationManager)
        historyFrame!!.setLocationRelativeTo(null)
        historyFrame!!.iconImage = icon

        // session summary frame
        sessionSummaryFrame = SessionSummaryFrame(
                categoryManager,
                sessionManager,
                configurationManager,
                timerManager)
        sessionSummaryFrame!!.setLocationRelativeTo(null)
        sessionSummaryFrame!!.iconImage = icon

        // stackmat developer frame
        stackmatDeveloperFrame = StackmatDeveloperFrame(timerManager)
        stackmatDeveloperFrame!!.setLocationRelativeTo(null)
        stackmatDeveloperFrame!!.iconImage = icon

        // category manager dialog
        categoryManagerDialog = CategoryManagerFrame(
                puzzleProvider,
                scramblerProvider,
                categoryManager,
                tipProvider)
        categoryManagerDialog!!.setLocationRelativeTo(null)
        categoryManagerDialog!!.iconImage = icon

        // color scheme frame
        colorSchemeFrame = ColorSchemeFrame(
                puzzleProvider,
                colorManager)
        colorSchemeFrame!!.setLocationRelativeTo(null)
        colorSchemeFrame!!.iconImage = icon
    }

    init {
        minimumSize = Dimension(800, 600)
        createComponents()
        pack()

        // timer configuration
        audioFormat = AudioFormat(8000, 8, 1, true, false)
        mixerInfo = null
        val stackmatTimerInputDeviceName = configurationManager.getConfiguration("STACKMAT-TIMER-INPUT-DEVICE")
        for (mixerInfo in AudioSystem.getMixerInfo()) {
            if (stackmatTimerInputDeviceName == mixerInfo.name) {
                this.mixerInfo = mixerInfo
                break
            }
        }
        setTimerTrigger(configurationManager.getConfiguration("TIMER-TRIGGER"))
        setTimerPrecision(configurationManager.getConfiguration("TIMER-PRECISION"))

        // inspection time sounds
        try {
            val inspectionClips = arrayOfNulls<Clip>(4)
            val fileNames = arrayOf("eight_seconds.wav", "go.wav", "plus_two.wav", "dnf.wav")
            for (i in inspectionClips.indices) {
                inspectionClips[i] = AudioSystem.getClip()
                inspectionClips[i].open(
                        AudioSystem.getAudioInputStream(BufferedInputStream(
                                MainFrame::class.java.getResourceAsStream("/com/puzzletimer/resources/inspection/" + fileNames[i])
                        )))
            }
            timerManager.addListener(object : TimerManager.Listener() {
                private var next = 0
                override fun inspectionStarted() {
                    next = 0
                }

                override fun inspectionRunning(remainingTime: Long) {
                    val soundStartTimes = intArrayOf(7000, 3000, 0, -2000, Int.MIN_VALUE)
                    if (remainingTime <= soundStartTimes[next]) {
                        inspectionClips[next]!!.framePosition = 0
                        inspectionClips[next]!!.start()
                        next++
                    }
                }
            })
        } catch (e: Exception) {
        }

        // title
        categoryManager.addListener(object : CategoryManager.Listener() {
            override fun categoriesUpdated(categories: Array<Category>?, currentCategory: Category?) {
                title = java.lang.String.format(
                        _("main.prisma_puzzle_time_category"),
                        currentCategory!!.getDescription())
            }
        })

        // Updates
        if (checkUpdate()) {
            val version = latest
            val versionNumber = getVersionNumber(version)
            val changelog = getDescription(version)
            val options = arrayOf<Any>("Update now!", "Cancel")
            val n = JOptionPane.showOptionDialog(
                    this,
                    _("update.message").toString() + " " + versionNumber + "\n" + changelog,
                    _("update.title"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0])
            if (n == 0) {
                val uf = UpdaterFrame("https://github.com/Moony22/prisma/releases/download/$versionNumber/prisma-$versionNumber.jar", versionNumber)
                uf.downloadLatestVersion()
                update = true
            }
        }

        // menuItemAddSolution
        menuItemAddSolution!!.addActionListener { e: ActionEvent? ->
            val now = Date()
            val solution = Solution(
                    UUID.randomUUID(),
                    categoryManager.getCurrentCategory().getCategoryId()!!,
                    scrambleManager.currentScramble!!,
                    Timing(now, now),
                    "",
                    "")
            val listener: SolutionEditingDialogListener = object : SolutionEditingDialogListener() {
                override fun solutionEdited(solution: Solution?) {
                    solutionManager.addSolution(solution!!)

                    // check for personal records
                    val measures = arrayOf(
                            Best(1, Int.MAX_VALUE),
                            BestMean(3, 3),
                            BestMean(100, 100),
                            BestAverage(5, 5),
                            BestAverage(12, 12),
                            BestAverage(50, 50))
                    val descriptions = arrayOf<String>(
                            _("main.single"),
                            _("main.mean_of_3"),
                            _("main.mean_of_100"),
                            _("main.average_of_5"),
                            _("main.average_of_12"),
                            _("main.average_of_50"))
                    val solutions = solutionManager.getSolutions()
                    val sessionSolutions = sessionManager.getSolutions()
                    for (i in measures.indices) {
                        if (sessionSolutions.size < measures[i].minimumWindowSize) {
                            continue
                        }
                        measures[i].setSolutions(solutions, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
                        val allTimeBest = measures[i].value
                        measures[i].setSolutions(sessionSolutions, configurationManager.getConfiguration("TIMER-PRECISION") == "CENTISECONDS")
                        val sessionBest = measures[i].value
                        if (measures[i].windowPosition == 0 && sessionBest <= allTimeBest) {
                            messageManager.enqueueMessage(
                                    MessageManager.MessageType.INFORMATION,
                                    java.lang.String.format(_("main.personal_record_message"),
                                            categoryManager.getCurrentCategory().getDescription(),
                                            formatMinutes(measures[i].value, configurationManager.getConfiguration("TIMER-PRECISION")!!, measures[i].round),
                                            descriptions[i]))
                        }
                    }
                    scrambleManager.changeScramble()
                }
            }
            val solutionEditingDialog = SolutionEditingDialog(this@MainFrame, true, solution, listener, configurationManager)
            solutionEditingDialog.title = _("main.add_solution_title")
            solutionEditingDialog.setLocationRelativeTo(null)
            solutionEditingDialog.isVisible = true
        }

        // menuItemExit
        menuItemExit!!.addActionListener { e: ActionEvent? -> System.exit(0) }

        // menuItemTips
        menuItemTips!!.addActionListener { e: ActionEvent? -> tipsFrame!!.isVisible = true }

        // menuItemScrambleQueue
        menuItemScrambleQueue!!.addActionListener { e: ActionEvent? -> scrambleQueueFrame!!.isVisible = true }

        // menuItemHistory
        menuItemHistory!!.addActionListener { e: ActionEvent? -> historyFrame!!.isVisible = true }

        // menuItemSessionSummary
        menuItemSessionSummary!!.addActionListener { e: ActionEvent? -> sessionSummaryFrame!!.isVisible = true }

        // menuItemStackmatDeveloper
        menuItemStackmatDeveloper!!.addActionListener { e: ActionEvent? ->
            val timerTrigger = configurationManager.getConfiguration("TIMER-TRIGGER")
            stackmatDeveloperFrame!!.isVisible = true
        }

        // menuCategory
        categoryManager.addListener(object : CategoryManager.Listener() {
            override fun categoriesUpdated(categories: Array<Category>?, currentCategory: Category?) {
                val menuShortcutKey = Toolkit.getDefaultToolkit().menuShortcutKeyMask
                menuCategory!!.removeAll()

                // category manager
                val menuItemCategoryManager = JMenuItem(_("main.category_manager"))
                menuItemCategoryManager.mnemonic = KeyEvent.VK_M
                menuItemCategoryManager.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, menuShortcutKey or InputEvent.ALT_MASK)
                menuItemCategoryManager.addActionListener { event: ActionEvent? -> categoryManagerDialog!!.isVisible = true }
                menuCategory!!.add(menuItemCategoryManager)
                menuCategory!!.addSeparator()
                val categoryGroup = ButtonGroup()

                // built-in categories
                class BuiltInCategory {
                    val category: Category? = null
                    val mnemonic = 0.toChar()
                    val accelerator = 0.toChar()
                    fun BuiltInCategory(category: Category?, mnemonic: Char, accelerator: Char) {
                        this.category = category
                        this.mnemonic = mnemonic
                        this.accelerator = accelerator
                    }
                }

                val builtInCategories = arrayOf(
                        BuiltInCategory(categories!![0], '2', '2'),
                        BuiltInCategory(categories[1], 'R', '3'),
                        BuiltInCategory(categories[2], 'O', 'O'),
                        BuiltInCategory(categories[3], 'B', 'B'),
                        BuiltInCategory(categories[4], 'F', 'F'),
                        BuiltInCategory(categories[5], '4', '4'),
                        BuiltInCategory(categories[6], 'B', '\u0000'),
                        BuiltInCategory(categories[7], '5', '5'),
                        BuiltInCategory(categories[8], 'B', '\u0000'),
                        BuiltInCategory(categories[9], '6', '6'),
                        BuiltInCategory(categories[10], '7', '7'),
                        BuiltInCategory(categories[11], 'C', 'K'),
                        BuiltInCategory(categories[12], 'M', 'M'),
                        BuiltInCategory(categories[13], 'P', 'P'),
                        BuiltInCategory(categories[14], 'S', '1'),
                        BuiltInCategory(categories[15], 'W', 'S')
                )
                for (builtInCategory in builtInCategories) {
                    val menuItemCategory = JRadioButtonMenuItem(builtInCategory.category!!.getDescription())
                    menuItemCategory.setMnemonic(builtInCategory.mnemonic)
                    if (builtInCategory.accelerator != '\u0000') {
                        menuItemCategory.accelerator = KeyStroke.getKeyStroke(builtInCategory.accelerator.toInt(), menuShortcutKey)
                    }
                    menuItemCategory.isSelected = builtInCategory.category == currentCategory
                    menuItemCategory.addActionListener { e: ActionEvent? -> categoryManager.setCurrentCategory(builtInCategory.category) }
                    menuCategory!!.add(menuItemCategory)
                    categoryGroup.add(menuItemCategory)
                }
                menuCategory!!.addSeparator()

                // user defined categories
                for (category in categories) {
                    if (category.isUserDefined) {
                        val menuItemCategory = JRadioButtonMenuItem(category.getDescription())
                        menuItemCategory.isSelected = category == currentCategory
                        menuItemCategory.addActionListener { e: ActionEvent? -> categoryManager.setCurrentCategory(category) }
                        menuCategory!!.add(menuItemCategory)
                        categoryGroup.add(menuItemCategory)
                    }
                }
            }
        })

        // menuColorScheme
        menuItemColorScheme!!.addActionListener { e: ActionEvent? -> colorSchemeFrame!!.isVisible = true }

        // menuItemInspectionTime
        menuItemInspectionTime!!.isSelected = timerManager.isInspectionEnabled()
        menuItemInspectionTime!!.addActionListener { e: ActionEvent? ->
            timerManager.setInspectionEnabled(
                    menuItemInspectionTime!!.isSelected)
        }

        // menuItemAnyKey
        menuItemAnyKey!!.isSelected = timerManager.isAnyKeyEnabled()
        menuItemAnyKey!!.addActionListener { e: ActionEvent? ->
            timerManager.setAnyKeyEnabled(
                    menuItemAnyKey!!.isSelected
            )
        }

        // menuDailySession
        menuDailySession!!.isSelected = sessionManager.isDailySessionEnabled
        menuDailySession!!.addActionListener { e: ActionEvent? -> sessionManager.isDailySessionEnabled = menuDailySession!!.isSelected }

        // menuItemHideTimer
        menuItemHideTimer!!.isSelected = timerManager.isHideTimerEnabled()
        menuItemHideTimer!!.addActionListener { e: ActionEvent? ->
            timerManager.setHideTimerEnabled(
                    menuItemHideTimer!!.isSelected
            )
        }

        // menuItemSmoothTiming
        menuItemSmoothTiming!!.isSelected = timerManager.isSmoothTimingEnabled()
        menuItemSmoothTiming!!.addActionListener { e: ActionEvent? ->
            timerManager.setSmoothTimingEnabled(
                    menuItemSmoothTiming!!.isSelected)
        }

        // menuItemManualInput
        menuItemManualInput!!.addActionListener { e: ActionEvent? -> setTimerTrigger("MANUAL-INPUT") }

        // menuItemCtrlKeys
        menuItemCtrlKeys!!.addActionListener { e: ActionEvent? -> setTimerTrigger("KEYBOARD-TIMER-CONTROL") }

        // menuItemSpaceKey
        menuItemSpaceKey!!.addActionListener { e: ActionEvent? -> setTimerTrigger("KEYBOARD-TIMER-SPACE") }

        // menuItemStackmatTimer
        menuItemStackmatTimer!!.addActionListener { e: ActionEvent? -> setTimerTrigger("STACKMAT-TIMER") }

        // menuItemDevice
        for (mixerInfo in AudioSystem.getMixerInfo()) {
            val targetLinesInfo = AudioSystem.getTargetLineInfo(DataLine.Info(TargetDataLine::class.java, audioFormat))
            var validMixer = false
            for (lineInfo in targetLinesInfo) {
                if (AudioSystem.getMixer(mixerInfo).isLineSupported(lineInfo)) {
                    validMixer = true
                    break
                }
            }
            if (!validMixer) {
                continue
            }
            val menuItemDevice = JRadioButtonMenuItem(mixerInfo.name)
            menuItemDevice.isSelected = stackmatTimerInputDeviceName == mixerInfo.name
            menuItemDevice.addActionListener { arg0: ActionEvent? ->
                this@MainFrame.mixerInfo = mixerInfo
                configurationManager.setConfiguration(
                        "STACKMAT-TIMER-INPUT-DEVICE", mixerInfo.name)
                val timerTrigger = configurationManager.getConfiguration("TIMER-TRIGGER")
                if (timerTrigger == "STACKMAT-TIMER") {
                    setTimerTrigger("STACKMAT-TIMER")
                }
            }
            stackmatTimerInputDevice!!.add(menuItemDevice)
            stackmatTimerInputDeviceGroup!!.add(menuItemDevice)
            if (this@MainFrame.mixerInfo == null) {
                menuItemDevice.isSelected = true
                this@MainFrame.mixerInfo = mixerInfo
                configurationManager.setConfiguration(
                        "STACKMAT-TIMER-INPUT-DEVICE", mixerInfo.name)
            }
        }
        menuItemDefaultLnF!!.addActionListener { e: ActionEvent? -> changeLookAndFeel(UIManager.getSystemLookAndFeelClassName(), this@MainFrame) }
        for (lafInfo in UIManager.getInstalledLookAndFeels()) {
            val lafMenuItem = JRadioButtonMenuItem(lafInfo.name)
            menuLookAndFeel!!.add(lafMenuItem)
            lookAndFeelGroup!!.add(lafMenuItem)
            if (UIManager.getLookAndFeel().name == lafInfo.name) {
                lafMenuItem.isSelected = true
            }
            lafMenuItem.addActionListener { e: ActionEvent? -> changeLookAndFeel(lafInfo.className, this@MainFrame) }
        }

        // menuItemCentiseconds
        menuItemCentiseconds!!.addActionListener { e: ActionEvent? -> setTimerPrecision("CENTISECONDS") }

        // menuItemMilliseconds
        menuItemMilliseconds!!.addActionListener { e: ActionEvent? -> setTimerPrecision("MILLISECONDS") }

        // menuItemFeedback
        menuItemFeedback!!.addActionListener { e: ActionEvent? ->
            try {
                Desktop.getDesktop().browse(URI("https://github.com/Moony22/prisma/issues/"))
            } catch (ex: Exception) {
                messageManager.enqueueMessage(MessageManager.MessageType.ERROR, "Failed to open feedback page: " + ex.localizedMessage)
            }
        }

        // menuItemAbout
        menuItemAbout!!.addActionListener { e: ActionEvent? ->
            val aboutDialog = AboutDialog(this@MainFrame, true)
            aboutDialog.setLocationRelativeTo(null)
            aboutDialog.isVisible = true
        }

        // menuItemExport
        exportSolutionsToCSV!!.addActionListener { ExportToFile(solutionManager) }

        // labelMessage
        messageManager.addListener(object : MessageManager.Listener() {
            override fun messagesCleared() {
                labelMessage!!.preferredSize = Dimension()
                labelMessage!!.isVisible = false
            }

            override fun messageReceived(messageType: MessageManager.MessageType?, message: String?) {
                labelMessage!!.preferredSize = Dimension(width, 30)
                if (messageType === MessageManager.MessageType.INFORMATION) {
                    labelMessage!!.background = Color(0x45, 0x73, 0xD5)
                } else if (messageType === MessageManager.MessageType.ERROR) {
                    labelMessage!!.background = Color(0xFF, 0x40, 0x40)
                }
                labelMessage!!.text = message
                labelMessage!!.isVisible = true
            }
        })
    }
}