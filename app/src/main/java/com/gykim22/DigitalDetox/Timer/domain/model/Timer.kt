package com.gykim22.DigitalDetox.Timer.domain.model

/**
 * 타이머의 상태와 초를 가지는 Data Class입니다.
 * @author Kim Giyun
 */
data class Timer(
    val status: TimerStatus = TimerStatus.STOPPED,
    val timerSecond: Long = 0L
)

/**
 * @param TimerStatus 타이머의 상태를 정의하는 Enum Class입니다.
 * @author gykim22
 */
enum class TimerStatus {
    RUNNING, PAUSED, STOPPED
}
