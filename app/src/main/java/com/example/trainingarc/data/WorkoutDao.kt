package com.example.trainingarc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trainingarc.model.Workout

@Dao
interface WorkoutDao {
    @Insert
    fun insert(workout: Workout)

    @Query("SELECT * FROM Workout WHERE userId = :userId")
    fun getWorkoutsForUser(userId: Int): List<Workout>
}
