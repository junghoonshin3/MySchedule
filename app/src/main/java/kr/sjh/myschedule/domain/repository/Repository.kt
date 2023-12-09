package kr.sjh.myschedule.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.data.repository.Result
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task
import java.time.LocalDate
import java.time.LocalDateTime

interface Repository {
    fun getSchedulesInRange(
        startDate: LocalDate, endDate: LocalDate
    ): Flow<Result<List<Schedule>>>

    fun deleteSchedules(schedule: Schedule)
    fun updateSchedules(schedules: List<Schedule>)
    fun insertSchedule(schedule: Schedule)
    fun insertScheduleWithTasks(schedule: Schedule, tasks: List<Task>)
    fun getScheduleWithTasks(): Flow<Result<Map<LocalDate, List<ScheduleWithTask>>>>
}