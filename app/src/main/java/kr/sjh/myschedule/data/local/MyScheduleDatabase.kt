package kr.sjh.myschedule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleDetailDao
import kr.sjh.myschedule.data.local.entity.ScheduleDetailEntity
import kr.sjh.myschedule.data.local.entity.ScheduleEntity

@Database(
    entities = [ScheduleEntity::class, ScheduleDetailEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ScheduleTypeConvert::class, DetailTypeConvert::class)
abstract class MyScheduleDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    abstract fun scheduleDetailDao(): ScheduleDetailDao

    companion object {
        const val DATABASE_NAME: String = "schedule_db"
    }
}