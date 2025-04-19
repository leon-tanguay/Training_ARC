package com.example.trainingarc.model

data class Workout(
    val workoutName: String,
    val username: String,
    val hourOf: Int,
    val minuteOf: Int
) {
    val twelveHourTime = if (hourOf % 12 == 0) 12 else hourOf % 12
    val timeString = String.format("%02d:%02d", hourOf, minuteOf)
}