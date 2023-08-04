package kr.sjh.myschedule.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kr.sjh.myschedule.data.local.entity.ScheduleDetailEntity

@Dao
interface ScheduleDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScheduleDetail(scheduleDetail: ScheduleDetailEntity): Long

    @Query("SELECT * from detail where userId =:userId")
    fun getScheduleDetail(userId: Int): ScheduleDetailEntity
}