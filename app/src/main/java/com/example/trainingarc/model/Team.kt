package com.example.trainingarc.model

data class Team(
    val name: String,
    val level: Int,
    val teammates: List<Teammate>
) {
    val totalPoints: Int
        get() = teammates.sumOf { it.points }

    val maxPoints: Int = 50000 // You can make this dynamic later if needed
}