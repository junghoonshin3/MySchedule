package kr.sjh.myschedule.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "schedules",
)
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String,
    val describe: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val color: Int
) : Parcelable