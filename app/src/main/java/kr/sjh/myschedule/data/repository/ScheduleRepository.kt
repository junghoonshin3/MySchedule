package kr.sjh.myschedule.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleDetailDao
import kr.sjh.myschedule.data.local.entity.ScheduleDetailEntity
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class ScheduleRepository constructor(
    private val scheduleDao: ScheduleDao,
    private val detailDao: ScheduleDetailDao
) {

    fun getAllScheduleWithDetailDao(regDt: LocalDate): Flow<List<ScheduleEntity>> = flow {
        try {
            emit(scheduleDao.getAllSchedules(regDt))
        } catch (e: Exception) {
            error(e)
        }

    }

    fun insertSchedule(schedule: ScheduleEntity) = scheduleDao.insertSchedule(schedule)

    fun insertScheduleDetail(detail: ScheduleDetailEntity) = detailDao.insertScheduleDetail(detail)

    fun getScheduleDetail(userId: Int) = detailDao.getScheduleDetail(userId)

}