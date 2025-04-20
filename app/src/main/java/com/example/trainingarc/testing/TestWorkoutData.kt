// File: testing/TestWorkoutData.kt
package com.example.trainingarc.testing

import android.content.Context
import com.example.trainingarc.model.*
import com.example.trainingarc.model.AppDatabase

suspend fun generateTestWorkoutFeed(context: Context): WorkoutFeed {
    val db = AppDatabase.getDatabase(context)

    val testProfile = Profile("greg", "greg’s g's", 25, 4, emptyList())

    val userId = db.userDao().insert(
        User(username = "greg", email = "", profile = testProfile)
    ).toInt()

    val commonYogaExercises = listOf(
        Exercise("Downward Dog", listOf(Set(60, 0, 0)), "Stretching"),
        Exercise("Child's Pose", listOf(Set(90, 0, 0)), "Stretching"),
        Exercise("Seated Forward Bend", listOf(Set(75, 0, 0)), "Stretching"),
        Exercise("Cat-Cow", listOf(Set(40, 0, 0)), "Stretching"),
        Exercise("Bridge Pose", listOf(Set(50, 0, 0)), "Stretching")
    )

    val workouts = buildList {
        add(
            Workout(
                workoutName = "Ultimate Full Body",
                userId = userId,
                hourOf = 7,
                minuteOf = 15,
                exercises = listOf(
                    Exercise("Push Ups", listOf(Set(0, 20, 0), Set(0, 15, 0)), "Bodyweight"),
                    Exercise("Squats", listOf(Set(0, 10, 100), Set(0, 8, 110)), "Weightlifting"),
                    Exercise("Deadlifts", listOf(Set(0, 5, 120), Set(0, 3, 130)), "Weightlifting"),
                    Exercise("Plank", listOf(Set(60, 0, 0), Set(45, 0, 0)), "Timed"),
                    Exercise("Jumping Jacks", listOf(Set(30, 0, 0)), "Cardio"),
                    Exercise("Lunges", listOf(Set(0, 12, 20), Set(0, 10, 25)), "Weightlifting")
                )
            )
        )

        repeat(8) {
            add(
                Workout(
                    workoutName = "Evening Yoga",
                    userId = userId,
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = commonYogaExercises
                )
            )
        }

        add(
            Workout(
                workoutName = "Legs & Core",
                userId = userId,
                hourOf = 17,
                minuteOf = 45,
                exercises = listOf(
                    Exercise("Lunges", listOf(Set(0, 12, 20), Set(0, 10, 25)), "Weightlifting"),
                    Exercise("Sit Ups", listOf(Set(0, 30, 0)), "Bodyweight")
                )
            )
        )
    }

    return WorkoutFeed(workouts)
}
