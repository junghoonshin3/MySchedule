package kr.sjh.myschedule.data.local.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleWithDetail(
    @Embedded val schedule: ScheduleEntity,
    @Relation(parentColumn = "id", entityColumn = "userId") val scheduleDetail: ScheduleDetailEntity
) : Parcelable