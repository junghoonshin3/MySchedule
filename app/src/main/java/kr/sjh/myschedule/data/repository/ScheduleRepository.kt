package kr.sjh.myschedule.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class ScheduleRepository constructor(
    private val scheduleDao: ScheduleDao
) {
    fun deleteSchedule(id: Long) = flow {
        try {
            emit(scheduleDao.deleteSchedule(id))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

    fun updateSchedule(schedule: ScheduleEntity) = flow {
        try {
            emit(scheduleDao.updateSchedule(schedule))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

    fun getYearSchedules(selectedDate: LocalDate) = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(scheduleDao.getYearSchedules(selectedDate)))
        } catch (e: Exception) {
            emit(Result.Fail(e))
        }
    }.flowOn(Dispatchers.IO)

    fun insertOrUpdate(schedule: ScheduleEntity) = flow {
        try {
            emit(scheduleDao.insertOrUpdate(schedule))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

}