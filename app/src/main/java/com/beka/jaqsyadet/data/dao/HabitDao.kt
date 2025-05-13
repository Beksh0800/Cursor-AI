package com.beka.jaqsyadet.data.dao

import androidx.room.*
import com.beka.jaqsyadet.data.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitById(habitId: Long): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("UPDATE habits SET streak = :streak, totalCompletions = totalCompletions + 1 WHERE id = :habitId")
    suspend fun updateHabitStreak(habitId: Long, streak: Int)

    @Query("SELECT * FROM habits WHERE reminderTime IS NOT NULL")
    fun getHabitsWithReminders(): Flow<List<Habit>>

    @Query("""
        SELECT * FROM habits 
        WHERE frequency = :frequency 
        AND (reminderDays IS NULL OR reminderDays LIKE '%' || :dayOfWeek || '%')
    """)
    fun getHabitsByFrequencyAndDay(frequency: String, dayOfWeek: String): Flow<List<Habit>>

    @Transaction
    @Query("""
        SELECT h.* FROM habits h
        LEFT JOIN habit_checks hc ON h.id = hc.habitId
        AND hc.completedAt >= :startOfDay
        AND hc.completedAt < :endOfDay
        WHERE hc.id IS NULL
    """)
    fun getUncompletedHabitsForDay(startOfDay: Long, endOfDay: Long): Flow<List<Habit>>
}