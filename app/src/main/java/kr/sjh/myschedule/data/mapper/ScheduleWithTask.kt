package kr.sjh.myschedule.data.mapper

import kr.sjh.myschedule.data.local.entity.ScheduleWithTask


fun List<ScheduleWithTask>.asDomain(): List<kr.sjh.myschedule.domain.model.ScheduleWithTask> = map {
    kr.sjh.myschedule.domain.model.ScheduleWithTask(
        it.schedule.asDomain(), it.tasks.asDomain()
    )
}