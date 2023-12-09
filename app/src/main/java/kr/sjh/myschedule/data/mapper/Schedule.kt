package kr.sjh.myschedule.data.mapper

import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.domain.model.Schedule

fun ScheduleEntity.asDomain(): Schedule = Schedule(
    id, title, describe,
)

fun List<ScheduleEntity>.asDomain(): List<Schedule> = map { it.asDomain() }

fun Schedule.toEntity(): ScheduleEntity = ScheduleEntity(
    id, title, describe
)

fun List<Schedule>.toEntityList(): List<ScheduleEntity> = map {
    it.toEntity()
}

