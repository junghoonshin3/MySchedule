package kr.sjh.myschedule.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.sjh.myschedule.data.local.MyScheduleDatabase
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleDetailDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    fun provideDB(@ApplicationContext context: Context): MyScheduleDatabase {
        return Room.databaseBuilder(
            context, MyScheduleDatabase::class.java,
            MyScheduleDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideScheduleDao(db: MyScheduleDatabase): ScheduleDao {
        return db.scheduleDao()
    }

    @Singleton
    @Provides
    fun provideScheduleDetailDao(db: MyScheduleDatabase): ScheduleDetailDao {
        return db.scheduleDetailDao()
    }
}