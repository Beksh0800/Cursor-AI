package com.beka.jaqsyadet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.beka.jaqsyadet.data.dao.HabitDao
import com.beka.jaqsyadet.data.dao.HabitCheckDao
import com.beka.jaqsyadet.data.model.Habit
import com.beka.jaqsyadet.data.model.HabitCheck
import com.beka.jaqsyadet.data.util.Converters

@Database(
    entities = [Habit::class, HabitCheck::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCheckDao(): HabitCheckDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jaqsy_adet_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 