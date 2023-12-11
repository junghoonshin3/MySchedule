package kr.sjh.myschedule.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleWithTaskDao
import kr.sjh.myschedule.data.local.dao.TaskDao
import kr.sjh.myschedule.data.mapper.asDomain
import kr.sjh.myschedule.data.mapper.toEntity
import kr.sjh.myschedule.data.mapper.toEntityList
import kr.sjh.myschedule.domain.model.Schedule
import kr.sjh.myschedule.domain.model.Task
import kr.sjh.myschedule.domain.repository.Repository
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class ScheduleRepositoryImpl constructor(
    private val scheduleDao: ScheduleDao,
    private val taskDao: TaskDao,
    private val scheduleWithTaskDao: ScheduleWithTaskDao
) : Repository {

    override fun deleteScheduleWithTasks(schedule: Schedule) {
        TODO("Not yet implemented")
    }

    override fun insertScheduleWithTasks(schedule: Schedule, tasks: List<Task>) {
        scheduleWithTaskDao.insertScheduleWithTasks(schedule.toEntity(), tasks.toEntityList())
    }

    override fun updateScheduleWithTasks(schedule: Schedule, tasks: List<Task>) {
        scheduleWithTaskDao.updateScheduleWithTasks(
            schedule.toEntity(), tasks.toEntityList()
        )
    }


    override fun getScheduleWithTasks(startDate: LocalDate, endDate: LocalDate) = flow {
        emit(Result.Loading)
        scheduleWithTaskDao.getScheduleWithTasks(startDate, endDate).map {
            it.mapValues {
                it.value.asDomain()
            }
        }.collect {
            emit(Result.Success(it))
        }
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)

}