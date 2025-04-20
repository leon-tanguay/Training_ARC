package com.example.trainingarc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded

data class Profile(
    val name: String,
    val team: String,
    val level: Int,
    val profilePicResId: Int,
    val badges: List<Int>
)