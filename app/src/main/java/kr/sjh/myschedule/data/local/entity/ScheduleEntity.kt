package kr.sjh.myschedule.data.local.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "schedules")
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "memo") var memo: String = "",
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "month") var month: Int,
    @ColumnInfo(name = "regDt") var regDt: LocalDate,
    @ColumnInfo(name = "color") val color: Int,
    @ColumnInfo(name = "alarmTime") var alarmTime: LocalTime,
    @ColumnInfo(name = "isAlarm") var isAlarm: Boolean = false,
    @ColumnInfo(name = "isComplete") var isComplete: Boolean = false,
) : Parcelable {
    constructor(
        title: String,
        memo: String,
        year: Int,
        month: Int,
        regDt: LocalDate,
        color: Int,
        alarmTime: LocalTime,
        isAlarm: Boolean,
        isComplete: Boolean
    ) : this(0, title, memo, year, month, regDt, color, alarmTime, isAlarm, isComplete)

    override fun toString(): String {
        return "${title},${memo},${year},${month},${regDt},${color},${alarmTime}, ${isAlarm},${isComplete}"
    }
}