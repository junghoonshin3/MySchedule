package kr.sjh.myschedule.data.local.dao

import androidx.room.*
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import java.time.LocalDate

@Dao
interface ScheduleDao {
    @Query("SELECT * from schedules where regDt =:regDt")
    fun getAllSchedules(regDt: LocalDate): List<ScheduleEntity>

    @Query("SELECT * from schedules where id =:id")
    fun getSchedule(id: Long): ScheduleEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleEntity): Long

    @Query("DELETE FROM schedules WHERE id = :id")
    fun deleteSchedule(id: Long)

}