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
            error(e)
        }
    }.flowOn(Dispatchers.IO)

    fun insertSchedule(
        schedule: ScheduleEntity
    ) {
        scheduleDao.insertSchedule(schedule)
    }

    fun deleteSchedule(id: Long) {
        scheduleDao.deleteSchedule(id)
    }

    fun getSchedule(id: Long): Flow<ScheduleEntity> = flow {
        try {
            emit(scheduleDao.getSchedule(id))
        } catch (e: Exception) {
            error(e)
        }
    }.flowOn(Dispatchers.IO)
}