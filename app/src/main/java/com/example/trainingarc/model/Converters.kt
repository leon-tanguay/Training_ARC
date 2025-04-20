package com.example.trainingarc.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val gson = Gson()
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

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

    // LocalDateTime
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}
