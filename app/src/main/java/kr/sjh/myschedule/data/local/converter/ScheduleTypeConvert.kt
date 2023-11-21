package kr.sjh.myschedule.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ScheduleTypeConvert {
    @TypeConverter
    fun localDateToString(localDate: LocalDate): String =
        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    @TypeConverter
    fun stringToLocalDate(string: String): LocalDate =
        LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    @TypeConverter
    fun localTimeToString(localTime: LocalTime): String =
        localTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    @TypeConverter
    fun stringToLocalTime(string: String): LocalTime =
        LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm"))

}