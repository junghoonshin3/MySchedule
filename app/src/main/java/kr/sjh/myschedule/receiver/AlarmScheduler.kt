package kr.sjh.myschedule.receiver

import kr.sjh.myschedule.data.local.entity.ScheduleEntity

interface AlarmScheduler {
    fun schedule(item: ScheduleEntity)
    fun cancel(item: ScheduleEntity)
}