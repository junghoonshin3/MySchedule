package kr.sjh.myschedule.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.sjh.myschedule.data.repository.ScheduleRepository
import kr.sjh.myschedule.data.local.dao.ScheduleDao
import kr.sjh.myschedule.data.local.dao.ScheduleDetailDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideScheduleRepository(
        schedule: ScheduleDao,
        schduleDetail: ScheduleDetailDao

    ): ScheduleRepository {
        return ScheduleRepository(
            schedule, schduleDetail
        )
    }

}