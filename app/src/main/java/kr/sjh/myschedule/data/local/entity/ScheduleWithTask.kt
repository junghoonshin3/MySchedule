package kr.sjh.myschedule.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ScheduleWithTask(
    @Embedded val schedule: ScheduleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "scheduleId"
    )
    val tasks: List<TaskEntity>
)