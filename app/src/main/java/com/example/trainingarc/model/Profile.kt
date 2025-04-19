package com.example.trainingarc.model

data class Profile(
    val name: String,
    val team: String,
    val level: Int,
    val profilePicResId: Int,
    val badges: List<Int> // Resource IDs of badge images
)
