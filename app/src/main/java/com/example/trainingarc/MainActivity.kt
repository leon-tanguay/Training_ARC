package com.example.trainingarc

import android.os.Bundle
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

    private lateinit var binding: ActivityMainBinding
    private lateinit var questOverlay: FrameLayout
    private lateinit var questListContainer: LinearLayout
    private lateinit var questButton: ImageButton

    private var dailyTimer: CountDownTimer? = null
    private var weeklyTimer: CountDownTimer? = null
    private lateinit var dailyHeader: TextView
    private lateinit var weeklyHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                R.id.navigation_notifications
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

        // Three daily quests
        val dailyQuests = listOf(
            "🏋️ Do 20 push‑ups",
            "💧 Drink 2L of water",
            "🚶 Take a 30‑minute walk"
        )
        dailyQuests.forEach { q ->
            questListContainer.addView(createQuestView(q))
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

        // Three weekly quests
        val weeklyQuests = listOf(
            "🏅 Complete 3 strength workouts",
            "🥗 Eat 5 servings of vegetables",
            "🛌 Get 7 hours of sleep each night"
        )
        weeklyQuests.forEach { q ->
            questListContainer.addView(createQuestView(q))
        }

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

    private fun createQuestView(text: String): TextView = TextView(this).apply {
        this.text = text
        setPadding(24, 16, 24, 16)
        textSize = 16f
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
        background = ContextCompat.getDrawable(
            context, android.R.drawable.dialog_holo_light_frame
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        dailyTimer?.cancel()
        weeklyTimer?.cancel()
        Log.d("MainActivity", "onDestroy: timers cancelled")
    }
}
