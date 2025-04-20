package com.example.trainingarc.model

import android.content.Context
import androidx.room.*
import com.example.trainingarc.data.WorkoutDao

@Database(entities = [Workout::class], version = 1)
@TypeConverters(Converters::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile private var INSTANCE: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_db" // ✅ separate DB file name
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}