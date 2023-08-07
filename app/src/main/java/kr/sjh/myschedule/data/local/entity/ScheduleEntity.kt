package kr.sjh.myschedule.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "schedules")
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "memo")
    var memo: String,
    @ColumnInfo(name = "regDt")
    var regDt: LocalDate,
    @ColumnInfo(name = "alarmTime")
    var alarmTime: LocalDateTime,
    @ColumnInfo(name = "isAlarm")
    var isAlarm: Boolean = false,
    @ColumnInfo(name = "isComplete")
    var isComplete: Boolean = false

) : Parcelable {
    constructor(
        title: String,
        memo: String,
        regDt: LocalDate,
        alarmTime: LocalDateTime,
        isAlarm: Boolean,
        isComplete: Boolean
    ) : this(0, title, memo, regDt, alarmTime, isAlarm, isComplete)
}