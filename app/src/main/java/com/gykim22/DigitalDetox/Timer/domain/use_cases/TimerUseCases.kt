package com.gykim22.DigitalDetox.Timer.domain.use_cases

import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import com.gykim22.DigitalDetox.di.PrimaryTimer
import com.gykim22.DigitalDetox.di.SubTimer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * TimerRepository로 타이머를 제어하는 UseCase입니다.
 * @author Kim Giyun
 */
class TimerUseCases @Inject constructor(
    @PrimaryTimer private val primaryTimerRepository: TimerRepository,
    @SubTimer private val subTimerRepository: TimerRepository
) {
    fun primaryFlow(): Flow<Long> =
        primaryTimerRepository.getTimerMillsUpdate()

    fun subFlow(): Flow<Long> =
        subTimerRepository.getTimerMillsUpdate()
}
