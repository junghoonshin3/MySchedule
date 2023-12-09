package kr.sjh.myschedule.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleWithTaskDao
import kr.sjh.myschedule.data.local.dao.TaskDao
import kr.sjh.myschedule.data.mapper.toEntity
import kr.sjh.myschedule.data.mapper.toEntityList
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task
import kr.sjh.myschedule.domain.repository.Repository
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class ScheduleRepository constructor(
    private val scheduleDao: ScheduleDao,
    private val taskDao: TaskDao,
    private val scheduleWithTaskDao: ScheduleWithTaskDao
) : Repository {


    override fun getSchedulesInRange(
        startDate: LocalDate, endDate: LocalDate
    ) = flow {
        emit(Result.Loading)
//        scheduleDao.getSchedulesInRange(startDate, endDate).map {
//            it.asDomain()
//        }.collect {
//            emit(Result.Success(it))
//        }
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)

    override fun insertSchedule(schedule: Schedule) {

    }

    override fun insertScheduleWithTasks(schedule: Schedule, tasks: List<Task>) {
        scheduleWithTaskDao.insertScheduleWithTasks(schedule.toEntity(), tasks.toEntityList())
    }

    override fun getScheduleWithTasks() = flow {
        emit(Result.Loading)
        scheduleWithTaskDao.getScheduleWithTasks().collect {
            emit(Result.Success(it))
        }
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)


    override fun deleteSchedules(schedule: Schedule) {
//        scheduleDao.deleteSchedule(schedule.toEntity())
    }

    override fun updateSchedules(schedules: List<Schedule>) {
//        scheduleDao.updateSchedule(*schedules.toEntityList().toTypedArray())
    }
}