package kr.sjh.myschedule.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    fun getAllSchedules(regDt: LocalDate): Flow<List<ScheduleEntity>> = flow {
        try {
            emit(scheduleDao.getAllSchedules(regDt))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

    fun insertSchedule(
        schedule: ScheduleEntity
    ) = flow {
        try {
            emit(scheduleDao.insertSchedule(schedule))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

    fun deleteSchedule(id: Long) = flow {
        try {
            emit(scheduleDao.deleteSchedule(id))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

    fun getSchedule(id: Long): Flow<ScheduleEntity> = flow {
        try {
            emit(scheduleDao.getSchedule(id))
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

}