package com.gykim22.DigitalDetox.Timer.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * TimerRepository 추상체입니다.
 * @author Kim Giyun
 */
interface TimerRepository {
    /**
     * 일정 주기로 현재 경과 시간(mills) Flow를 반환합니다.
     * @return Flow<Long>
     */
    fun getTimerMillsUpdate(): Flow<Long>

    /**
     * 타이머를 시작합니다.
     */
    fun startTimer()

    /**
     * 타이머를 일시정지합니다.
     */
    fun pauseTimer()

    /**
     * 타이머 기능을 종료하고,시간을 초기화합니다.
     */
    fun stopTimer()
}