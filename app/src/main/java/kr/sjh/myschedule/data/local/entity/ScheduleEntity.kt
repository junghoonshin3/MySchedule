package kr.sjh.myschedule.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "schedules",
)
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val title: String,
    val describe: String,
) : Parcelable