package com.puzzletimer

import com.puzzletimer.database.ConfigurationDAO
import com.puzzletimer.database.ColorDAO
import com.puzzletimer.database.CategoryDAO
import com.puzzletimer.database.SolutionDAO
import com.puzzletimer.puzzles.PuzzleProvider
import com.puzzletimer.parsers.ScrambleParserProvider
import com.puzzletimer.scramblers.ScramblerProvider
import com.puzzletimer.tips.TipProvider
import kotlin.jvm.JvmStatic
import javax.swing.SwingUtilities
import java.lang.Runnable
import javax.swing.UIManager
import java.awt.Image
import com.puzzletimer.gui.MainFrame
import javax.swing.JFrame
import java.lang.ClassNotFoundException
import javax.swing.JOptionPane
import com.puzzletimer.Internationalization
import java.io.File
import java.sql.DriverManager
import java.util.Objects
import org.h2.tools.RunScript
import java.sql.SQLException
import java.util.Arrays
import java.sql.ResultSet
import com.puzzletimer.database.DatabaseException
import com.puzzletimer.models.*
import java.util.UUID
import com.puzzletimer.statistics.StatisticalMeasure
import com.puzzletimer.statistics.Best
import com.puzzletimer.statistics.BestMean
import com.puzzletimer.statistics.BestAverage
import com.puzzletimer.util.SolutionUtils
import com.puzzletimer.state.*
import com.puzzletimer.timer.Timer
import java.awt.Toolkit
import java.io.InputStreamReader
import java.io.Reader
import java.lang.Exception
import java.sql.Connection

class Main {
    private val configurationDAO: ConfigurationDAO
    private val colorDAO: ColorDAO
    private val categoryDAO: CategoryDAO
    private val solutionDAO: SolutionDAO
    private val messageManager: MessageManager
    private val configurationManager: ConfigurationManager
    private val timerManager: TimerManager
    private val puzzleProvider: PuzzleProvider
    private val colorManager: ColorManager
    private val scrambleParserProvider: ScrambleParserProvider
    private val scramblerProvider: ScramblerProvider
    private val tipProvider: TipProvider
    private val categoryManager: CategoryManager
    private val scrambleManager: ScrambleManager
    private val solutionManager: SolutionManager
    private val sessionManager: SessionManager

    companion object {
        private const val DATABASE_RESOURCE_LOCATION = "/com/puzzletimer/resources/database/"
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater(object : Runnable {
                override fun run() {
                    val main = Main()
                    var laf = main.configurationManager.getConfiguration("LOOK-AND-FEEL")
                    if (laf == null) laf = UIManager.getSystemLookAndFeelClassName()
                    try {
                        UIManager.setLookAndFeel(laf)
                    } catch (ignored: Exception) {
                    }
                    val icon = Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/com/puzzletimer/resources/icon.png"))

                    // main frame
                    val mainFrame = MainFrame(
                            main.messageManager,
                            main.configurationManager,
                            main.timerManager,
                            main.puzzleProvider,
                            main.colorManager,
                            main.scrambleParserProvider,
                            main.scramblerProvider,
                            main.tipProvider,
                            main.categoryManager,
                            main.scrambleManager,
                            main.solutionManager,
                            main.sessionManager,
                            main.solutionDAO)
                    mainFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                    mainFrame.setLocationRelativeTo(null)
                    mainFrame.iconImage = icon
                    main.categoryManager.currentCategory = main.categoryManager.currentCategory
                    if (main.configurationManager.getConfiguration("DAILYSESSION-ENABLED") == "TRUE") {
                        val sols = main.solutionDAO.getCurrentSession(main.categoryManager.currentCategory)
                        for (solution in sols) {
                            main.sessionManager.addSolution(solution)
                        }
                    }
                    if (!mainFrame.hasUpdate()) mainFrame.isVisible = true
                }
            })
        }
    }

    init {
        // load database driver
        try {
            Class.forName("org.h2.Driver")
        } catch (e: ClassNotFoundException) {
            val frame = JFrame()
            JOptionPane.showMessageDialog(
                    frame,
                    Internationalization.keyify("main.database_driver_load_error"),
                    Internationalization.keyify("main.prisma_puzzle_timer"),
                    JOptionPane.ERROR_MESSAGE)
            System.exit(0)
        }

        // create initial database if necessary
        val databaseFile = File("puzzletimer.h2.db")
        if (!databaseFile.exists()) {
            try {
                val connection = DriverManager.getConnection("jdbc:h2:puzzletimer", "sa", "")
                val script: Reader = InputStreamReader(
                        Objects.requireNonNull(javaClass.getResourceAsStream(
                                DATABASE_RESOURCE_LOCATION + "puzzletimer0.3.sql")))
                RunScript.execute(connection, script)
                connection.close()
            } catch (e: SQLException) {
                val frame = JFrame()
                JOptionPane.showMessageDialog(
                        frame, String.format(Internationalization.keyify("main.database_error_message"), e.message),
                        Internationalization.keyify("main.prisma_puzzle_timer"),
                        JOptionPane.ERROR_MESSAGE)
                System.exit(0)
            }
        }

        // connect to database
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection("jdbc:h2:puzzletimer;IFEXISTS=TRUE", "sa", "")
        } catch (e: SQLException) {
            val frame = JFrame()
            JOptionPane.showMessageDialog(
                    frame,
                    Internationalization.keyify("main.concurrent_database_access_error_message"),
                    Internationalization.keyify("main.prisma_puzzle_timer"),
                    JOptionPane.ERROR_MESSAGE)
            System.exit(0)
        }

        // update database if necessary
        val versions = Arrays.asList("0.3", "0.4", "0.5", "0.6", "0.9", "0.9.3", "0.10.0")
        var currentVersion = ""
        try {
            val statement = connection!!.createStatement()
            val resultSet = statement.executeQuery(
                    "SELECT VALUE FROM CONFIGURATION WHERE KEY = 'VERSION'")
            while (resultSet.next()) {
                currentVersion = resultSet.getString(1)
            }
        } catch (ignored: SQLException) {
        }
        val maxVersionIndex = versions.size - 1
        var versionIndex = versions.indexOf(currentVersion)
        if (versionIndex < 0) versionIndex = maxVersionIndex
        versionIndex++
        while (versionIndex <= maxVersionIndex) {
            try {
                val scriptName = "puzzletimer" + versions[versionIndex] + ".sql"
                val script: Reader = InputStreamReader(
                        Objects.requireNonNull(javaClass.getResourceAsStream(
                                DATABASE_RESOURCE_LOCATION + scriptName)))
                RunScript.execute(connection, script)
            } catch (e: SQLException) {
                val frame = JFrame()
                JOptionPane.showMessageDialog(
                        frame, String.format(Internationalization.keyify("main.database_error_message"), e.message),
                        Internationalization.keyify("main.prisma_puzzle_timer"),
                        JOptionPane.ERROR_MESSAGE)
                System.exit(0)
            }
            versionIndex++
        }

        // message manager
        messageManager = MessageManager()

        // configuration DAO
        configurationDAO = ConfigurationDAO(connection)

        // configuration manager
        configurationManager = ConfigurationManager(configurationDAO.all)
        configurationManager.addListener(object : ConfigurationManager.Listener() {
            override fun configurationEntryUpdated(key: String, value: String) {
                try {
                    configurationDAO.update(
                            ConfigurationEntry(key, value))
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }
        })

        // timer manager
        timerManager = TimerManager()
        timerManager.isInspectionEnabled = configurationManager.getConfiguration("INSPECTION-TIME-ENABLED") == "TRUE"
        timerManager.isAnyKeyEnabled = configurationManager.getConfiguration("ANYKEY-ENABLED") == "TRUE"
        timerManager.isHideTimerEnabled = configurationManager.getConfiguration("HIDETIMER-ENABLED") == "TRUE"
        timerManager.isSmoothTimingEnabled = configurationManager.getConfiguration("SMOOTH-TIMING-ENABLED") == "TRUE"
        timerManager.addListener(object : TimerManager.Listener() {
            override fun solutionFinished(timing: Timing, penalty: String) {
                // add solution
                solutionManager.addSolution(
                        Solution(
                                UUID.randomUUID(),
                                categoryManager.currentCategory.categoryId,
                                scrambleManager.currentScramble,
                                timing,
                                penalty,
                                ""))

                // check for personal records
                val measures = arrayOf(
                        Best(1, Int.MAX_VALUE),
                        BestMean(3, 3),
                        BestMean(100, 100),
                        BestAverage(5, 5),
                        BestAverage(12, 12),
                        BestAverage(50, 50))
                val descriptions = arrayOf(
                        Internationalization.keyify("main.single"),
                        Internationalization.keyify("main.mean_of_3"),
                        Internationalization.keyify("main.mean_of_100"),
                        Internationalization.keyify("main.average_of_5"),
                        Internationalization.keyify("main.average_of_12"),
                        Internationalization.keyify("main.average_of_50"))
                val solutions = solutionManager.solutions
                val sessionSolutions = sessionManager.solutions
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
                                MessageManager.MessageType.INFORMATION, String.format(Internationalization.keyify("main.personal_record_message"),
                                categoryManager.currentCategory.description,
                                SolutionUtils.formatMinutes(measures[i].value, configurationManager.getConfiguration("TIMER-PRECISION"), measures[i].round),
                                descriptions[i]))
                    }
                }

                // gerate next scramble
                scrambleManager.changeScramble()
            }

            override fun timerChanged(timer: Timer) {
                configurationManager.setConfiguration(
                        "TIMER-TRIGGER", timer.timerId)
            }

            override fun inspectionEnabledSet(inspectionEnabled: Boolean) {
                configurationManager.setConfiguration(
                        "INSPECTION-TIME-ENABLED", if (inspectionEnabled) "TRUE" else "FALSE")
            }

            override fun anyKeyEnabledSet(anyKeyEnabled: Boolean) {
                configurationManager.setConfiguration(
                        "ANYKEY-ENABLED", if (anyKeyEnabled) "TRUE" else "FALSE")
            }

            override fun hideTimerEnabledSet(hideTimerEnabled: Boolean) {
                configurationManager.setConfiguration(
                        "HIDETIMER-ENABLED", if (hideTimerEnabled) "TRUE" else "FALSE")
            }

            override fun precisionChanged(timerPrecisionId: String) {
                configurationManager.setConfiguration("TIMER-PRECISION", timerPrecisionId)
            }

            override fun smoothTimingSet(smoothTimingEnabled: Boolean) {
                configurationManager.setConfiguration(
                        "SMOOTH-TIMING-ENABLED", if (smoothTimingEnabled) "TRUE" else "FALSE")
            }
        })

        // puzzle provider
        puzzleProvider = PuzzleProvider()

        // color DAO
        colorDAO = ColorDAO(connection)

        // color manager
        colorManager = ColorManager(colorDAO.all)
        colorManager.addListener(object : ColorManager.Listener() {
            override fun colorSchemeUpdated(colorScheme: ColorScheme) {
                try {
                    colorDAO.update(colorScheme)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }
        })

        // scramble parser provider
        scrambleParserProvider = ScrambleParserProvider()

        // scrambler provider
        scramblerProvider = ScramblerProvider()


        // tip provider
        tipProvider = TipProvider()

        // category DAO
        categoryDAO = CategoryDAO(connection)

        // categoryManager
        val categories = categoryDAO.all
        val currentCategoryId = UUID.fromString(
                configurationManager.getConfiguration("CURRENT-CATEGORY"))
        var currentCategory: Category? = null
        for (category in categories) {
            if (category.categoryId == currentCategoryId) {
                currentCategory = category
            }
        }
        categoryManager = CategoryManager(categories, currentCategory)
        categoryManager.addListener(object : CategoryManager.Listener() {
            override fun currentCategoryChanged(category: Category) {
                configurationManager.setConfiguration(
                        "CURRENT-CATEGORY",
                        category.categoryId.toString())
                try {
                    solutionManager.loadSolutions(
                            solutionDAO.getAll(category))
                    sessionManager.clearSession()
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }

            override fun categoryAdded(category: Category) {
                try {
                    categoryDAO.insert(category)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }

            override fun categoryRemoved(category: Category) {
                try {
                    categoryDAO.delete(category)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }

            override fun categoryUpdated(category: Category) {
                try {
                    categoryDAO.update(category)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }
        })

        // scramble manager
        scrambleManager = ScrambleManager(
                scramblerProvider,
                scramblerProvider[currentCategory!!.scramblerId])
        categoryManager.addListener(object : CategoryManager.Listener() {
            override fun currentCategoryChanged(category: Category) {
                scrambleManager.setCategory(category)
            }
        })

        // solution DAO
        solutionDAO = SolutionDAO(connection, scramblerProvider, scrambleParserProvider)

        // solution manager
        solutionManager = SolutionManager()
        solutionManager.addListener(object : SolutionManager.Listener() {
            override fun solutionAdded(solution: Solution) {
                sessionManager.addSolution(solution)
                try {
                    solutionDAO.insert(solution)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }

            override fun solutionsAdded(solutions: Array<Solution>) {
                try {
                    solutionDAO.insert(solutions)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }

            override fun solutionUpdated(solution: Solution) {
                sessionManager.updateSolution(solution)
                try {
                    solutionDAO.update(solution)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }

            override fun solutionRemoved(solution: Solution) {
                sessionManager.removeSolution(solution)
                try {
                    solutionDAO.delete(solution)
                } catch (e: DatabaseException) {
                    messageManager.enqueueMessage(
                            MessageManager.MessageType.ERROR, String.format(Internationalization.keyify("main.database_error_message"), e.message))
                }
            }
        })

        // session manager
        sessionManager = SessionManager()
        sessionManager.isDailySessionEnabled = configurationManager.getConfiguration("DAILYSESSION-ENABLED") == "TRUE"
        sessionManager.addListener(object : SessionManager.Listener() {
            override fun dailySessionSet(dailySessionEnabled: Boolean) {
                configurationManager.setConfiguration(
                        "DAILYSESSION-ENABLED", if (dailySessionEnabled) "TRUE" else "FALSE")
            }
        })
    }
}