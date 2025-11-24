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

/**
 * TimerRepository 구현체입니다.
 * @author Kim Giyun
 */
class TimerRepositoryImpl @Inject constructor() : TimerRepository {

    private val _flow = MutableStateFlow(0L)
    override fun getTimerMillsUpdate(): Flow<Long> = _flow.asStateFlow()

    private var timerJob: Job? = null
    private var lastTimestamp = 0L
    private var accumulated = 0L

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun startTimer() {
        if (timerJob != null) return

        lastTimestamp = System.currentTimeMillis()

        timerJob = scope.launch {
            while (true) {
                delay(100)

                val now = System.currentTimeMillis()
                val delta = now - lastTimestamp
                lastTimestamp = now

                accumulated += delta
                _flow.value = accumulated
            }
        }
    }

    override fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun stopTimer() {
        timerJob?.cancel()
        timerJob = null

        accumulated = 0L
        lastTimestamp = 0L
        _flow.value = 0L
    }
}