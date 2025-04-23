package com.davanok.taskapp.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

object DatabaseConverters {
    @TypeConverter
    fun stringToList(value: String): List<String> =
        if (value.isBlank()) emptyList()
        else value.split(';')
    @TypeConverter
    fun listToString(value: List<String>): String = value.joinToString(";")

    @TypeConverter
    fun longToInstant(value: Long): Instant = Instant.fromEpochSeconds(value)
    @TypeConverter
    fun instantToLong(value: Instant): Long = value.epochSeconds
}