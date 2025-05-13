package com.beka.jaqsyadet.data.dao

import androidx.room.*
import com.beka.jaqsyadet.data.model.HabitCheck
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitCheckDao {
    @Query("SELECT * FROM habit_checks WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun getHabitChecks(habitId: Long): Flow<List<HabitCheck>>

    @Insert
    suspend fun insertHabitCheck(habitCheck: HabitCheck): Long

    @Delete
    suspend fun deleteHabitCheck(habitCheck: HabitCheck)

    @Query("SELECT * FROM habit_checks WHERE habitId = :habitId AND completedAt >= :startTime AND completedAt <= :endTime")
    suspend fun getHabitChecksForPeriod(habitId: Long, startTime: Long, endTime: Long): List<HabitCheck>

    @Query("SELECT COUNT(*) FROM habit_checks WHERE habitId = :habitId AND completedAt >= :startTime")
    suspend fun getCompletionCountSince(habitId: Long, startTime: Long): Int
} 