package kr.sjh.myschedule.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.data.repository.Result
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task
import java.time.LocalDate

interface Repository {
    fun deleteSchedules(schedule: Schedule)
    fun updateSchedules(schedules: List<Schedule>)
    fun insertSchedule(schedule: Schedule)
    fun insertScheduleWithTasks(schedule: Schedule, tasks: List<Task>)
    fun updateScheduleWithTasks(schedule: Schedule, tasks: List<Task>)
    fun getScheduleWithTasks(
        startDate: LocalDate, endDate: LocalDate
    ): Flow<Result<Map<LocalDate, List<ScheduleWithTask>>>>
}