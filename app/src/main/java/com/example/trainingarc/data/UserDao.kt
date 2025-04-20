package com.example.trainingarc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trainingarc.model.User
import androidx.room.Update


@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM User WHERE userId = :id")
    fun getUser(id: Int): User

    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    fun getUserByUsername(username: String): User?

    @Query("UPDATE user SET points = :points WHERE userId = :userId")
    fun updatePoints(userId: Int, points: Int)

    @Update
    fun updateUser(user: User)
}