package kr.sjh.myschedule.data.local

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailTypeConvert {
    @TypeConverter
    fun localDateTimeToString(localDate: LocalDateTime): String =
        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    @TypeConverter
    fun stringToLocalDateTime(string: String): LocalDateTime =
        LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}