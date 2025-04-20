// File: testing/TestWorkoutData.kt
package com.example.trainingarc.testing

import android.content.Context
import com.example.trainingarc.model.*
import com.example.trainingarc.model.AppDatabase

suspend fun generateTestWorkoutFeed(context: Context): WorkoutFeed {
    val db = AppDatabase.getDatabase(context)

    val users = listOf(
        User(username = "greg", email = "", profile = Profile("Greg", "greg’s g's", profilePicResId = 25, badges = emptyList())),
        User(username = "kim", email = "", profile = Profile("Kim", "kim's crushers", profilePicResId = 26, badges = emptyList())),
        User(username = "ava", email = "", profile = Profile("Ava", "ava’s avocados", profilePicResId = 27, badges = emptyList())),
        User(username = "matt", email = "", profile = Profile("Matt", "matt’s mountains", profilePicResId = 28, badges = emptyList()))
    )

    val userIds = users.map { user ->
        db.userDao().insert(user).toInt()
    }

    val commonYogaExercises = listOf(
        Exercise("Downward Dog", listOf(Set(60, 0, 0)), "Stretching"),
        Exercise("Child's Pose", listOf(Set(90, 0, 0)), "Stretching"),
        Exercise("Seated Forward Bend", listOf(Set(75, 0, 0)), "Stretching"),
        Exercise("Cat-Cow", listOf(Set(40, 0, 0)), "Stretching"),
        Exercise("Bridge Pose", listOf(Set(50, 0, 0)), "Stretching")
    )

    val workouts = mutableListOf<Workout>()

    // Add a full-body workout for Greg
    workouts.add(
        Workout(
            workoutName = "Greg's Ultimate Full Body",
            userId = userIds[0],
            hourOf = 7,
            minuteOf = 15,
            exercises = listOf(
                Exercise("Push Ups", listOf(Set(0, 20, 0), Set(0, 15, 0)), "Bodyweight"),
                Exercise("Squats", listOf(Set(0, 10, 100), Set(0, 8, 110)), "Weightlifting"),
                Exercise("Deadlifts", listOf(Set(0, 5, 120), Set(0, 3, 130)), "Weightlifting")
            )
        )
    )

    // Add yoga for each user
    userIds.forEachIndexed { index, id ->
        repeat(2 + index) {  // Kim gets 2, Ava 3, Matt 4
            workouts.add(
                Workout(
                    workoutName = "${users[index].username.capitalize()}'s Yoga",
                    userId = id,
                    hourOf = 18,
                    minuteOf = 0,
                    exercises = commonYogaExercises
                )
            )
        }
    }

    // Add one unique workout per user
    workouts.add(
        Workout(
            workoutName = "Kim's HIIT",
            userId = userIds[1],
            hourOf = 6,
            minuteOf = 45,
            exercises = listOf(
                Exercise("Burpees", listOf(Set(0, 15, 0)), "Cardio"),
                Exercise("Mountain Climbers", listOf(Set(0, 30, 0)), "Cardio")
            )
        )
    )

    workouts.add(
        Workout(
            workoutName = "Ava's Core Blast",
            userId = userIds[2],
            hourOf = 10,
            minuteOf = 0,
            exercises = listOf(
                Exercise("Plank", listOf(Set(60, 0, 0)), "Timed"),
                Exercise("Bicycle Crunches", listOf(Set(0, 30, 0)), "Bodyweight")
            )
        )
    )

    workouts.add(
        Workout(
            workoutName = "Matt's Strength",
            userId = userIds[3],
            hourOf = 14,
            minuteOf = 30,
            exercises = listOf(
                Exercise("Bench Press", listOf(Set(0, 5, 135)), "Weightlifting"),
                Exercise("Pull Ups", listOf(Set(0, 8, 0)), "Bodyweight")
            )
        )
    )

    return WorkoutFeed(workouts)
}

