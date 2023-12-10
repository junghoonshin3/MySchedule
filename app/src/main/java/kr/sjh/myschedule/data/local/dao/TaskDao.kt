package kr.sjh.myschedule.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(vararg task: TaskEntity)

    @Update
    fun updateTasks(vararg task: TaskEntity)

    @Query("DELETE FROM tasks WHERE scheduleId = :scheduleId")
    fun deleteTasks(scheduleId: Long)

    @Query("SELECT * FROM tasks WHERE scheduleId = :id")
    fun getTasks(id: Long): List<TaskEntity>
}