package com.gykim22.DigitalDetox.di

import com.gykim22.DigitalDetox.Timer.data.repository.TimerRepositoryImpl
import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

/**
 * Timer Module입니다.
 * ViewModelComponent에 의존성을 주입합니다.
 * @author Kim Giyun
 */
@Module
@InstallIn(ViewModelComponent::class)
object TimerModule {
    @PrimaryTimer
    @Provides
    @ViewModelScoped
    fun providePrimaryTimerRepository(): TimerRepository =
        TimerRepositoryImpl()

    @SubTimer
    @Provides
    @ViewModelScoped
    fun provideSubTimerRepository(): TimerRepository =
        TimerRepositoryImpl()
}

/**
 * 동일 구현체에 Qualifier 어노테이션을 활용해 name tag를 달아 식별합니다.
 * @author Kim Giyun
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PrimaryTimer

/**
 * 동일 구현체에 Qualifier 어노테이션을 활용해 name tag를 달아 식별합니다.
 * @author Kim Giyun
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SubTimer