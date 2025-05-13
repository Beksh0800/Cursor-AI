package com.beka.jaqsyadet.data.repository

import com.beka.jaqsyadet.data.dao.HabitDao
import com.beka.jaqsyadet.data.dao.HabitCheckDao
import com.beka.jaqsyadet.data.model.Habit
import com.beka.jaqsyadet.data.model.HabitCheck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val habitCheckDao: HabitCheckDao
) {
    fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()
        .catch { e ->
            // Log error or emit empty list
            emit(emptyList())
        }

    suspend fun getHabitById(id: Long): Result<Habit> = try {
        habitDao.getHabitById(id)?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Habit not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun insertHabit(habit: Habit): Result<Long> = try {
        Result.success(habitDao.insertHabit(habit))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateHabit(habit: Habit): Result<Unit> = try {
        Result.success(habitDao.updateHabit(habit))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteHabit(habit: Habit): Result<Unit> = try {
        Result.success(habitDao.deleteHabit(habit))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun markHabitAsCompleted(habitId: Long): Result<Unit> = try {
        val habit = habitDao.getHabitById(habitId) ?: throw NoSuchElementException("Habit not found")
        val check = HabitCheck(habitId = habitId)
        habitCheckDao.insertHabitCheck(check)

        // Calculate start of day in milliseconds
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        val completionsToday = habitCheckDao.getCompletionCountSince(habitId, startOfDay)

        if (completionsToday == 1) { // First completion today
            val newStreak = habit.streak + 1
            habitDao.updateHabitStreak(habitId, newStreak)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getHabitChecks(habitId: Long): Flow<List<HabitCheck>> =
        habitCheckDao.getHabitChecks(habitId)
            .catch { e ->
                // Log error or emit empty list
                emit(emptyList())
            }

    suspend fun getHabitChecksForPeriod(
        habitId: Long,
        startTime: Long,
        endTime: Long
    ): Result<List<HabitCheck>> = try {
        Result.success(habitCheckDao.getHabitChecksForPeriod(habitId, startTime, endTime))
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getHabitsWithReminders(): Flow<List<Habit>> =
        habitDao.getHabitsWithReminders()
            .catch { e ->
                // Log error or emit empty list
                emit(emptyList())
            }

    fun getUncompletedHabitsForDay(startOfDay: Long, endOfDay: Long): Flow<List<Habit>> =
        habitDao.getUncompletedHabitsForDay(startOfDay, endOfDay)
            .catch { e ->
                // Log error or emit empty list
                emit(emptyList())
            }

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getEndOfDay(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
} 