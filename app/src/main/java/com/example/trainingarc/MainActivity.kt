package com.example.trainingarc

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.trainingarc.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var questOverlay: FrameLayout
    private lateinit var questListContainer: LinearLayout
    private lateinit var questButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout with view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up top app bar
        setSupportActionBar(binding.topAppBar)

        // Set up bottom navigation
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

        // Initialize dropdown overlay views
        questOverlay = findViewById(R.id.questDropdownOverlay)
        questListContainer = findViewById(R.id.questListContainer)
        questButton = findViewById(R.id.questButton)

        // Toggle dropdown on button click
        questButton.setOnClickListener {
            if (questOverlay.visibility == View.VISIBLE) {
                questOverlay.visibility = View.GONE
            } else {
                questOverlay.visibility = View.VISIBLE
                showDailyQuests()
            }
        }

        // Dismiss overlay when clicking outside
        questOverlay.setOnClickListener {
            questOverlay.visibility = View.GONE
        }
    }

    // Dynamically populate daily quests
    private fun showDailyQuests() {
        questListContainer.removeAllViews()

        val quests = listOf(
            "🏋️ Do 20 push-ups",
            "💧 Drink 2L of water",
            "🚶 Take a 30-minute walk"
        )

        for (quest in quests) {
            val questView = TextView(this).apply {
                text = quest
                setPadding(24, 16, 24, 16)
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                background = ContextCompat.getDrawable(context, android.R.drawable.dialog_holo_light_frame)
            }
            questListContainer.addView(questView)
        }
    }
}
