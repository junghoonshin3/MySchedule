package kr.sjh.myschedule.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import javax.inject.Singleton

@Singleton
class ScheduleRepository constructor(
    private val scheduleDao: ScheduleDao
) {
    fun deleteSchedule(id: Long) = flow {
        emit(scheduleDao.deleteSchedule(id))
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)

    fun updateSchedule(schedule: ScheduleEntity) = flow {
        emit(scheduleDao.updateSchedule(schedule))
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)

    fun getYearSchedulesInRange(startYear: Int, endYear: Int) = flow {
        emit(Result.Loading)
        scheduleDao.getYearSchedulesInRange(startYear, endYear).collect {
            emit(Result.Success(it))
        }
    }.catch {
        emit(Result.Fail(it))
    }.flowOn(Dispatchers.IO)

    fun insertOrUpdate(schedule: ScheduleEntity) = flow {
        emit(scheduleDao.insertOrUpdate(schedule))
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)

    fun insertScheduleList(list: List<ScheduleEntity>) = flow {
        emit(scheduleDao.insertScheduleList(*list.toTypedArray()))
    }.catch {
        it.printStackTrace()
        error(it)
    }.flowOn(Dispatchers.IO)
}