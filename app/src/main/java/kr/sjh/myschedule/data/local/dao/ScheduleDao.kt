package kr.sjh.myschedule.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.data.local.entity.TaskEntity
import kr.sjh.myschedule.domain.model.Schedule
import java.time.LocalDate

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: ScheduleEntity): Long

    @Update
    fun updateSchedule(schedule: ScheduleEntity)

    @Delete
    fun deleteSchedule(schedule: ScheduleEntity)

    @Query("SELECT * FROM schedules")
    fun getSchedules(): Flow<List<ScheduleEntity>>


//    @Query("SELECT * FROM schedules WHERE selectedDates BETWEEN :startYear AND :endYear")
//    fun getSchedulesInRange(
//        startYear: LocalDate, endYear: LocalDate
//    ): Flow<List<ScheduleEntity>>
//
//    @Query("SELECT * from schedules where id =:id")
//    fun getSchedule(id: Long): ScheduleEntity
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertSchedule(schedule: ScheduleEntity): Long
//
//    @Query("SELECT * from schedules WHERE id= :id")
//    fun getScheduleById(id: Long): List<ScheduleEntity>
//
//    @Update
//    fun updateSchedule(vararg schedule: ScheduleEntity): Int
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertSchedules(vararg schedule: ScheduleEntity)
//
//    @Delete
//    fun deleteSchedule(entity: ScheduleEntity): Int


}

