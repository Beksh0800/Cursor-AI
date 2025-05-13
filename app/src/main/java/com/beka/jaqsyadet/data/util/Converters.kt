package com.beka.jaqsyadet.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.util.Calendar

class Converters {

    private val gson = Gson()

    /**
     * Преобразует Set<DayOfWeek> в JSON-строку для хранения в базе данных.
     */
    @TypeConverter
    fun fromDayOfWeekSet(value: Set<DayOfWeek>): String {
        // Преобразуем DayOfWeek (enum) → Set<Int>
        val intSet = value.map { it.value }.toSet()
        return gson.toJson(intSet)
    }

    /**
     * Преобразует JSON-строку обратно в Set<DayOfWeek>.
     */
    @TypeConverter
    fun toDayOfWeekSet(value: String): Set<DayOfWeek> {
        val listType = object : TypeToken<Set<Int>>() {}.type
        val intSet: Set<Int> = gson.fromJson(value, listType) ?: emptySet()
        return intSet.mapNotNull {
            try {
                DayOfWeek.of(it)
            } catch (e: Exception) {
                null
            }
        }.toSet()
    }
    companion object {
        // Calendar-based day of week constants for API compatibility
        const val SUNDAY = Calendar.SUNDAY
        const val MONDAY = Calendar.MONDAY
        const val TUESDAY = Calendar.TUESDAY
        const val WEDNESDAY = Calendar.WEDNESDAY
        const val THURSDAY = Calendar.THURSDAY
        const val FRIDAY = Calendar.FRIDAY
        const val SATURDAY = Calendar.SATURDAY

        fun getDayName(dayOfWeek: Int): String {
            return when (dayOfWeek) {
                SUNDAY -> "Sunday"
                MONDAY -> "Monday"
                TUESDAY -> "Tuesday"
                WEDNESDAY -> "Wednesday"
                THURSDAY -> "Thursday"
                FRIDAY -> "Friday"
                SATURDAY -> "Saturday"
                else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
            }
        }

        fun getDayOfWeek(dayName: String): Int {
            return when (dayName.lowercase()) {
                "sunday" -> SUNDAY
                "monday" -> MONDAY
                "tuesday" -> TUESDAY
                "wednesday" -> WEDNESDAY
                "thursday" -> THURSDAY
                "friday" -> FRIDAY
                "saturday" -> SATURDAY
                else -> throw IllegalArgumentException("Invalid day name: $dayName")
            }
        }
    }
}
