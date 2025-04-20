package com.example.trainingarc.ui.quests

data class QuestDropdownManager (
    val dailyQuests: List<String>,
    val weeklyQuests: List<String>
) {
    val allQuests: List<String>
        get() = dailyQuests + weeklyQuests
}
