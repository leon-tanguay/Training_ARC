package com.example.trainingarc

import android.os.Bundle
import android.widget.CheckBox
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.trainingarc.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var loggedInUserId: Int = -1

    private lateinit var binding: ActivityMainBinding
    private lateinit var questOverlay: FrameLayout
    private lateinit var questListContainer: LinearLayout
    private lateinit var questButton: ImageButton

    private var dailyTimer: CountDownTimer? = null
    private var weeklyTimer: CountDownTimer? = null
    private lateinit var dailyHeader: TextView
    private lateinit var weeklyHeader: TextView

    private val questCompletionMap = mutableMapOf<String, Boolean>()

    private val allDailyQuests = listOf(
        "Stretch - quick 15 minute stretch",
        "Superset - a little extra push after a big effort",
        "Friendly - interact with a friend’s workout",
        "Mental reset - meditate for 5 minutes",
        "Treasure hunt - try new exercise not tried before",
        "Take a walk - 15 minute outdoor walk free from distractions",
        "Band together - use resistance bands in today’s workout",
        "1% better - add a unit to any one workout"
    )

    private val allWeeklyQuests = listOf(
        "All rounder - hit every muscle in a week",
        "Sporty - play a sport",
        "Level up - improve an exercise (time/rep/weight)",
        "Squad up - workout with a teammate",
        "Core crusher - hit core 3 times in a week",
        "Stretch sage - follow 10 minute flexibility video on youtube",
        "Mat master - complete a workout on a mat"
    )

    private fun updateUserPoints(pointsToAdd: Int) {
        Thread {
            val db = com.example.trainingarc.model.AppDatabase.getDatabase(applicationContext)
            val userDao = db.userDao()
            val user = userDao.getUser(loggedInUserId)

            if (user != null) {
                val newTotalPoints = user.points + pointsToAdd
                val newWeeklyPoints = user.pointsThisWeek + pointsToAdd

                val updatedUser = user.copy(
                    points = newTotalPoints,
                    pointsThisWeek = newWeeklyPoints
                )

                userDao.updateUser(updatedUser)

                val newLevel = updatedUser.level
                val progress = updatedUser.progressToNextLevel

                Log.d("XP_DEBUG", "Points updated: ${updatedUser.points}, Weekly: ${updatedUser.pointsThisWeek}, Level: $newLevel, Progress: $progress/100")
            }
        }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        loggedInUserId = prefs.getInt("logged_in_user_id", -1)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_team,
                R.id.navigation_profile,
                R.id.navigation_home,
                R.id.navigation_workout,
                R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        questOverlay = findViewById(R.id.questDropdownOverlay)
        questListContainer = findViewById(R.id.questListContainer)
        questButton = findViewById(R.id.questButton)

        questButton.setOnClickListener {
            if (questOverlay.visibility == View.VISIBLE) hideQuestsOverlay()
            else showQuests()
        }
        questOverlay.setOnClickListener { hideQuestsOverlay() }
    }

    private fun hideQuestsOverlay() {
        questOverlay.visibility = View.GONE
        dailyTimer?.cancel()
        weeklyTimer?.cancel()
        Log.d("MainActivity", "Timers cancelled and overlay hidden")
    }

    /**
     * Formats a countdown. If showDays is false and days == 0, omits the days part.
     */
    private fun formatCountdown(millis: Long, showDays: Boolean = true): String {
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        val dayPart = if (showDays || days > 0) "${days}d " else ""
        return "$dayPart${hours}h ${minutes}m ${seconds}s"
    }

    private fun getMillisUntilEndOfDay(): Long {
        val cal = Calendar.getInstance()
        val now = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        if (cal.timeInMillis <= now) cal.add(Calendar.DAY_OF_YEAR, 1)
        return cal.timeInMillis - now
    }

    private fun getMillisUntilEndOfWeek(): Long {
        val cal = Calendar.getInstance()
        val now = cal.timeInMillis
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        if (cal.timeInMillis <= now) cal.add(Calendar.WEEK_OF_YEAR, 1)
        return cal.timeInMillis - now
    }

    private fun showQuests() {
        Log.d("MainActivity", "showQuests() called")
        questOverlay.visibility = View.VISIBLE
        questListContainer.removeAllViews()
        dailyTimer?.cancel()
        weeklyTimer?.cancel()



        // Daily countdown header (omit '0d ' when less than a day)
        val initialDayMs = getMillisUntilEndOfDay()
        dailyHeader = TextView(this).apply {
            text = "Daily Quests: ${formatCountdown(initialDayMs, showDays = false)}"
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
            setPadding(0, 0, 0, 8)
        }
        questListContainer.addView(dailyHeader)

        // Fixed daily quests
        val dailyQuests = listOf(
            "🏋️ Go to the gym",
            "🏃 Cardio",
            "🎯 " + getTodayDailyQuest()
        )

        dailyQuests.forEach { quest ->
            questListContainer.addView(createQuestView(quest))
        }



        // Weekly countdown header (show days always)
        val initialWeekMs = getMillisUntilEndOfWeek()
        weeklyHeader = TextView(this).apply {
            text = "Weekly Quests: ${formatCountdown(initialWeekMs, showDays = true)}"
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
            setPadding(0, 16, 0, 8)
        }
        questListContainer.addView(weeklyHeader)

        // Randomized weekly quests
        val thisWeekQuest = getThisWeekWeeklyQuest()
        questListContainer.addView(createQuestView(thisWeekQuest))


        // Start timers
        dailyTimer = object : CountDownTimer(initialDayMs, 1000) {
            override fun onTick(ms: Long) {
                dailyHeader.text = "Daily Quests: ${formatCountdown(ms, showDays = false)}"
            }
            override fun onFinish() {
                dailyHeader.text = "Daily Quests: 0h 0m 0s"
            }
        }.start()

        weeklyTimer = object : CountDownTimer(initialWeekMs, 1000) {
            override fun onTick(ms: Long) {
                weeklyHeader.text = "Weekly Quests: ${formatCountdown(ms, showDays = true)}"
            }
            override fun onFinish() {
                weeklyHeader.text = "Weekly Quests: 0d 0h 0m 0s"
            }
        }.start()
    }

    private fun createQuestView(text: String): LinearLayout {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 12, 16, 12)
            background = ContextCompat.getDrawable(
                context, android.R.drawable.dialog_holo_light_frame
            )
        }

        val checkBox = CheckBox(this).apply {
            isChecked = questCompletionMap[text] ?: false
            setOnCheckedChangeListener { _, isChecked ->
                questCompletionMap[text] = isChecked

                if (isChecked) {
                    val lowerText = text.lowercase()
                    val isDaily = allDailyQuests.any { lowerText.contains(it.lowercase()) } || lowerText.contains("go to the gym") || lowerText.contains("cardio")
                    val isWeekly = allWeeklyQuests.any { lowerText.contains(it.lowercase()) }

                    val pointsToAdd = when {
                        isDaily -> 10
                        isWeekly -> 50
                        else -> 0
                    }

                    if (pointsToAdd > 0) {
                        updateUserPoints(pointsToAdd)
                    }
                }
            }
            setPadding(0, 0, 16, 0)
        }

        val textView = TextView(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

        container.addView(checkBox)
        container.addView(textView)

        return container
    }



    private fun getTodayDailyQuest(): String {
        val calendar = Calendar.getInstance()
        val seed = calendar.get(Calendar.DAY_OF_YEAR) + calendar.get(Calendar.YEAR)
        val random = java.util.Random(seed.toLong())
        return allDailyQuests.shuffled(random).first()
    }

    private fun getThisWeekWeeklyQuest(): String {
        val calendar = Calendar.getInstance()
        val seed = calendar.get(Calendar.WEEK_OF_YEAR) + calendar.get(Calendar.YEAR)
        val random = java.util.Random(seed.toLong())
        return allWeeklyQuests.shuffled(random).first()
    }



    override fun onDestroy() {
        super.onDestroy()
        dailyTimer?.cancel()
        weeklyTimer?.cancel()
        Log.d("MainActivity", "onDestroy: timers cancelled")
    }
}
