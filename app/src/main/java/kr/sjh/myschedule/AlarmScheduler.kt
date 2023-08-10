package kr.sjh.myschedule

import kr.sjh.myschedule.data.local.entity.ScheduleEntity

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}