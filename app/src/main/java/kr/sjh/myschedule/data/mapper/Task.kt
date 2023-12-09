package kr.sjh.myschedule.data.mapper

import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.local.entity.TaskEntity
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task

fun TaskEntity.asDomain(): Task = Task(
    id, scheduleId, regDt
)

fun List<TaskEntity>.asDomain(): List<Task> = map { it.asDomain() }

fun Task.toEntity(): TaskEntity = TaskEntity(
    id, scheduleId, regDt
)

fun List<Task>.toEntityList(): List<TaskEntity> = map {
    it.toEntity()
}