package kr.sjh.myschedule.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.local.entity.ScheduleWithTask
import kr.sjh.myschedule.data.local.entity.TaskEntity
import java.time.LocalDate

@Dao
interface ScheduleWithTaskDao : ScheduleDao, TaskDao {
    @MapInfo(keyColumn = "regDt")
    @Query("SELECT * FROM schedules INNER JOIN tasks ON schedules.id = tasks.scheduleId")
    fun getScheduleWithTasks(): Flow<Map<LocalDate, List<ScheduleWithTask>>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScheduleWithTasks(schedule: ScheduleEntity, tasks: List<TaskEntity>) {
        val id = insertSchedule(schedule)
        val list = tasks.map {
            it.apply {
                scheduleId = id
            }
        }
        insertTasks(*list.toTypedArray())
    }
}