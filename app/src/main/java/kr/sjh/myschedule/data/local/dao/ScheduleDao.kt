package kr.sjh.myschedule.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleEntity

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE year BETWEEN :startYear AND :endYear ")
    fun getYearSchedulesInRange(startYear: Int, endYear: Int): Flow<List<ScheduleEntity>>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScheduleList(vararg schedule: ScheduleEntity)

    @Query("DELETE FROM schedules WHERE id = :id")
    fun deleteSchedule(id: Long): Int


}

