package kr.sjh.myschedule.data.local.dao

import android.util.Log
import androidx.room.*
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import java.time.LocalDate

@Dao
interface ScheduleDao {
    @Query("SELECT * from schedules where regDt =:regDt and isComplete = 0")
    fun getAllSchedules(regDt: LocalDate): List<ScheduleEntity>

    @Query("SELECT * from schedules where regDt BETWEEN :startDt AND :endDt AND isComplete = 0")
    fun getAllBetweenSchedulesByGroup(startDt: LocalDate, endDt: LocalDate): List<ScheduleEntity>

    @Query("SELECT * FROM schedules WHERE strftime('%Y', regDt) = strftime('%Y', :selectedDate) AND isComplete = 0")
    fun getYearSchedules(selectedDate: LocalDate): List<ScheduleEntity>

    @Query("SELECT * from schedules where id =:id")
    fun getSchedule(id: Long): ScheduleEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleEntity): Long

    @Query("SELECT * from schedules WHERE id= :id")
    fun getScheduleById(id: Long): List<ScheduleEntity>

    @Update
    fun updateSchedule(schedule: ScheduleEntity): Int

    fun insertOrUpdate(schedule: ScheduleEntity): Long {
        val scheduleList = getScheduleById(schedule.id)
        return if (scheduleList.isEmpty())
            insertSchedule(schedule)
        else {
            updateSchedule(schedule)
            schedule.id
        }

    }

    @Query("DELETE FROM schedules WHERE id = :id")
    fun deleteSchedule(id: Long): Int


}

