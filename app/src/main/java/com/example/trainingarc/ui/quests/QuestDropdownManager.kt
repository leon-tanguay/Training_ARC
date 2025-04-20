package com.example.trainingarc.ui.quest

object QuestDropdownManager {
    val dailyQuests = listOf("Do 10 pushups", "Walk 5,000 steps", "Stretch for 5 min")
    val weeklyQuests = listOf("Workout 3x", "Try a new exercise", "Reach 10,000 points")

    val allQuests: List<String>
        get() = dailyQuests + weeklyQuests
}
