package com.example.trainingarc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity
@TypeConverters(Converters::class) // Optional if you're registering it globally
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutName: String,
    val userId: Int,
    val hourOf: Int,
    val minuteOf: Int,
    val exercises: List<Exercise>,
    val startDateTime: LocalDateTime = LocalDateTime.now() // NEW FIELD
)
