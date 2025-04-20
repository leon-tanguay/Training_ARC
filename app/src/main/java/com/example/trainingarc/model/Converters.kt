package com.example.trainingarc.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // List<Int> (badges)
    @TypeConverter
    fun fromIntList(value: List<Int>?): String = gson.toJson(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    // List<Set>
    @TypeConverter
    fun fromSetList(value: List<Set>?): String = gson.toJson(value)

    @TypeConverter
    fun toSetList(value: String): List<Set> {
        val type = object : TypeToken<List<Set>>() {}.type
        return gson.fromJson(value, type)
    }

    // List<Exercise>
    @TypeConverter
    fun fromExerciseList(value: List<Exercise>?): String = gson.toJson(value)

    @TypeConverter
    fun toExerciseList(value: String): List<Exercise> {
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, type)
    }
}
