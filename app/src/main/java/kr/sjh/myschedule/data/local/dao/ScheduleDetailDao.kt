package kr.sjh.myschedule.data.local.dao

//@Dao
//interface ScheduleDetailDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertScheduleDetail(scheduleDetail: ScheduleDetailEntity): Long
//
//    @Query("SELECT * from detail where userId =:userId")
//    fun getScheduleDetail(userId: Int): ScheduleDetailEntity
//
//    @Query("DELETE FROM detail WHERE userId = :userId")
//    fun deleteScheduleDetail(userId: Long)
//}