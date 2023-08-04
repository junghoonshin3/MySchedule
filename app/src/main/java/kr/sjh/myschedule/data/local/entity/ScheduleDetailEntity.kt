package kr.sjh.myschedule.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "detail")
@Parcelize
data class ScheduleDetailEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "userId")
    var userId: Int,
    @ColumnInfo(name = "alarmTime")
    var alarmTime: LocalDateTime,
    @ColumnInfo(name = "isAlarm")
    var isAlarm: Boolean = false

) : Parcelable