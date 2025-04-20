package com.example.trainingarc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String,
    val email: String,
    val points: Int = 0,
    val pointsThisWeek: Int = 0,

    @Embedded(prefix = "profile_")
    val profile: Profile? = null
) {
    val level: Int
        get() = (points / 100) + 1

    val progressToNextLevel: Int
        get() = points % 100
}
