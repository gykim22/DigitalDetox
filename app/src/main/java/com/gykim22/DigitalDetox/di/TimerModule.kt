package com.gykim22.DigitalDetox.di

import com.gykim22.DigitalDetox.Timer.data.repository.TimerRepositoryImpl
import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimerModule {
    /* 인터페이스(TimerRepository)에 구현체(TimerRepositoryImpl)를 바인딩 */
    @Binds
    @Singleton
    abstract fun bindTimerRepository(
        timerRepositoryImpl: TimerRepositoryImpl
    ): TimerRepository
}