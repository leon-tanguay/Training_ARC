package com.example.trainingarc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutName: String,
    val userId: Int,
    val hourOf: Int,
    val minuteOf: Int,
    val exercises: List<Exercise> // Room uses @TypeConverter to handle this
)