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
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "memo")
    var memo: String,
    @ColumnInfo(name = "regDt")
    var regDt: LocalDate

) : Parcelable