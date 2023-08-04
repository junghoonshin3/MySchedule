package kr.sjh.myschedule.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleTypeConvert {
    @TypeConverter
    fun localDateToString(localDate: LocalDate): String =
        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    @TypeConverter
    fun stringToLocalDate(string: String): LocalDate =
        LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}