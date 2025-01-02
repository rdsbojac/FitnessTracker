package com.example.fitnesstracker.model

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?) : Date? = dateLong?.let { Date (it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
}