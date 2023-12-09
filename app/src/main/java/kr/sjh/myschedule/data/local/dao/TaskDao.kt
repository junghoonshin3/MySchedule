package kr.sjh.myschedule.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.sjh.myschedule.data.local.entity.ScheduleEntity
import kr.sjh.myschedule.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert
    fun insertTasks(vararg task: TaskEntity)

    @Update
    fun updateTasks(vararg task: TaskEntity)

    @Delete
    fun deleteTasks(vararg task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>
}