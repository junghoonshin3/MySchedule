package kr.sjh.myschedule.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleWithTaskDao
import kr.sjh.myschedule.data.local.dao.TaskDao
import kr.sjh.myschedule.data.repository.ScheduleRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideScheduleRepository(
        schedule: ScheduleDao, task: TaskDao, scheduleWithTask: ScheduleWithTaskDao
    ): ScheduleRepositoryImpl {
        return ScheduleRepositoryImpl(
            schedule, task, scheduleWithTask
        )
    }
}