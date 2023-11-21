package kr.sjh.myschedule.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.ui.DateSelection
import kr.sjh.myschedule.utill.Common.ADD_PAGE
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

    fun getYearSchedules(year: Int) = flow {
        emit(Result.Loading)
        scheduleDao.getYearSchedules(year).collect {
            emit(Result.Success(it))
        }
    }.catch {
        emit(Result.Fail(it))
    }

    fun insertOrUpdate(schedule: ScheduleEntity, dateSelection: DateSelection?) = flow {
        try {
            Log.i("sjh", "$schedule")
            emit(scheduleDao.insertOrUpdate(schedule))
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }.flowOn(Dispatchers.IO)

//    fun getSchedule(userId: Long) = flow {
//        emit(Result.Loading)
//        try {
//            if (userId == ADD_PAGE) {
//                emit(Result.Success(ScheduleEntity()))
//            } else {
//                emit(Result.Success(scheduleDao.getSchedule(userId)))
//            }
//
//        } catch (e: Exception) {
//            error(Result.Fail(e))
//        }
//    }

}