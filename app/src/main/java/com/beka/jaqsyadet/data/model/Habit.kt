package com.beka.jaqsyadet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val frequency: String, // DAILY, WEEKLY, etc.
    val reminderTime: String? = null, // HH:mm format
    val reminderDays: Set<DayOfWeek> = emptySet(), // For weekly habits
    val streak: Int = 0,
    val totalCompletions: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)