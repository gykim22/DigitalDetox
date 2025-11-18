package com.gykim22.DigitalDetox.Timer.data.repository

import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TimerRepository 구현체입니다.
 * @author Kim Giyun
 */
@Singleton
class TimerRepositoryImpl @Inject constructor() : TimerRepository {
    /**
     * 타이머 현재 경과 시간
     * 변수 명 뒤에 Millis를 붙여 이 변수가 어떤 단위인지 표기.
     **/
    private val _timeElapsedMillis = MutableStateFlow(0L)

    private var timerJob: Job? = null

    /**
     * 타이머의 최신 재개 시간
     **/
    private var startTimeMillis: Long = 0L

    /**
     * 일시 정지에 따른 타이머 누적 시간
     **/
    private var accumulatedTimeMillis: Long = 0L

    private val timerScope = CoroutineScope(Dispatchers.Default)

    override fun getTimerMillsUpdate(): Flow<Long> {
        return _timeElapsedMillis.asStateFlow()
    }

    /**
     * 타이머 시작
     */
    override fun startTimer() {
        if (timerJob?.isActive == true) return
        startTimeMillis = System.currentTimeMillis()

        timerJob = timerScope.launch {
            while (true) {
                /* Compose에서 delay가 없다면 UI는 event를 잔뜩 받고 성능이 저하됨. 따라서, 적절한 딜레이로 정확도와 성능 개선 */
                delay(30)

                val currentTimeMillis = System.currentTimeMillis()
                val elapsedTimeMillis = currentTimeMillis - startTimeMillis + accumulatedTimeMillis

                _timeElapsedMillis.value = elapsedTimeMillis
            }
        }
    }

    /**
     * 타이머 일시 정지
     */
    override fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null

        /* 경과 시간을 누적 시간에 저장 */
        accumulatedTimeMillis = _timeElapsedMillis.value
    }

    /**
     * 타이머 종료
     */
    override fun stopTimer() {
        timerJob?.cancel()
        timerJob = null

        /* 전부 초기화 */
        startTimeMillis = 0L
        accumulatedTimeMillis = 0L
        _timeElapsedMillis.value = 0L
    }

}