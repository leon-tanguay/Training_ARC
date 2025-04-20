package com.example.trainingarc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trainingarc.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM User WHERE userId = :id")
    fun getUser(id: Int): User

    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    fun getUserByUsername(username: String): User?
}