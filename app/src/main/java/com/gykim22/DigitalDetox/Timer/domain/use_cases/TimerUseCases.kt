package com.gykim22.DigitalDetox.Timer.domain.use_cases

import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * TimerRepository로 타이머를 제어하는 UseCase입니다.
 * @author Kim Giyun
 */
class TimerUseCases @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Flow<Long> {
        return timerRepository.getTimerMillsUpdate()
    }

    fun startTimer() {
        timerRepository.startTimer()
    }

    fun pauseTimer() {
        timerRepository.pauseTimer()
    }

    fun stopTimer() {
        timerRepository.stopTimer()
    }
}
