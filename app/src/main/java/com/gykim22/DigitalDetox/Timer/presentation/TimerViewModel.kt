package com.gykim22.DigitalDetox.Timer.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gykim22.DigitalDetox.Timer.domain.model.Timer
import com.gykim22.DigitalDetox.Timer.domain.model.TimerStatus
import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import com.gykim22.DigitalDetox.Timer.domain.use_cases.TimerUseCases
import com.gykim22.DigitalDetox.di.PrimaryTimer
import com.gykim22.DigitalDetox.di.SubTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.timer

/**
 * 주 타이머와, 보조 타이머를 정의합니다.
 * @property primaryTimer 주 타이머 객체
 * @property subTimer 보조 타이머 객체
 * @author Kim Giyun
 */
data class TimerList(
    val primaryTimer: Timer,
    val subTimer: Timer,
)

sealed class TimerUiEvent {
    data class NavigateToAddNote(
        val total: Long,
        val study: Long,
        val rest: Long
    ) : TimerUiEvent()
}

/**
 * 타이머 뷰모델입니다.
 * @property timerState 타이머 상태
 * @property timerUseCases 타이머 유스케이스
 * @property repository 타이머 레포지토리
 * @author Kim Giyun
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerUseCases: TimerUseCases,
    @PrimaryTimer private val primaryTimerRepository: TimerRepository,
    @SubTimer private val subTimerRepository: TimerRepository
) : ViewModel() {
    private val _timerState = MutableStateFlow(TimerList(
        primaryTimer = Timer(
            timerSecond = 0,
            status = TimerStatus.STOPPED
        ),
        subTimer = Timer(
            timerSecond = 0,
            status = TimerStatus.STOPPED
        )
    ))
    val timerState: StateFlow<TimerList> = _timerState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<TimerUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private suspend fun sendEvent(event: TimerUiEvent) {
        _uiEvent.emit(event)
    }

    init {
        collectPrimaryTimerState()
        collectSubTimerState()
    }

    /**
     * Primary Timer 시간 변경 Flow 수집
     */
    private fun collectPrimaryTimerState() {
        viewModelScope.launch {
            timerUseCases.primaryFlow().collect { mills ->
                _timerState.value = _timerState.value.copy(
                    primaryTimer = _timerState.value.primaryTimer.copy(
                        timerSecond = mills
                    )
                )
                Log.d("TimerRepo", "primary tick: $mills")
            }
        }
    }

    /**
     * Sub Timer 시간 변경 Flow 수집
     */
    private fun collectSubTimerState() {
        viewModelScope.launch {
            timerUseCases.subFlow().collect { mills ->
                _timerState.value = _timerState.value.copy(
                    subTimer = _timerState.value.subTimer.copy(
                        timerSecond = mills
                    )
                )
                Log.d("TimerRepo", "sub tick: $mills")

            }
        }
    }

    /**
     * 전체 Timer의 시작과 일시정지를 제어하는 함수입니다.
     */
    fun startPauseTimer() {
        when (_timerState.value.primaryTimer.status) {
            TimerStatus.RUNNING -> {
                pausePrimaryTimer()
                startSubTimer()
            }
            TimerStatus.PAUSED, TimerStatus.STOPPED -> {
                startPrimaryTimer()
                pauseSubTimer()
            }
        }
    }

    /**
     * 전체 Timer를 종료하는 함수입니다.
     */
    fun stopTimer() {
        val primary = _timerState.value.primaryTimer.timerSecond
        val sub = _timerState.value.subTimer.timerSecond
        val total = primary + sub

        primaryTimerRepository.stopTimer()
        subTimerRepository.stopTimer()

        _timerState.value = _timerState.value.copy(
            primaryTimer = _timerState.value.primaryTimer.copy(status = TimerStatus.STOPPED),
            subTimer = _timerState.value.subTimer.copy(status = TimerStatus.STOPPED)
        )

        viewModelScope.launch {
            sendEvent(
                TimerUiEvent.NavigateToAddNote(
                    total = total,
                    study = primary,
                    rest = sub
                )
            )
        }
    }


    /**
     * 앱이 백그라운드에 진입할 시 타이머를 제어하는 함수입니다.
     */
    fun onApplicationBackgroundTimer() {
        when(_timerState.value.primaryTimer.status) {
            TimerStatus.RUNNING -> {
                startSubTimer()
                pausePrimaryTimer()
            }

            TimerStatus.PAUSED, TimerStatus.STOPPED -> {}
        }
    }

    /**
     * 앱이 포어그라운드에 진입할 시 타이머를 제어하는 함수입니다.
     */
    fun onApplicationForegroundTimer() {
        when(_timerState.value.primaryTimer.status) {
            TimerStatus.PAUSED -> {
                pauseSubTimer()
                startPrimaryTimer()
            }
            TimerStatus.RUNNING, TimerStatus.STOPPED -> { }
        }
    }

    /**
     * 헬퍼 함수
     */
    private fun startPrimaryTimer() {
        primaryTimerRepository.startTimer()
        updatePrimaryStatus(TimerStatus.RUNNING)
    }

    private fun pausePrimaryTimer() {
        primaryTimerRepository.pauseTimer()
        updatePrimaryStatus(TimerStatus.PAUSED)
    }

    private fun startSubTimer() {
        subTimerRepository.startTimer()
        updateSubStatus(TimerStatus.RUNNING)
    }

    private fun pauseSubTimer() {
        subTimerRepository.pauseTimer()
        updateSubStatus(TimerStatus.PAUSED)
    }

    private fun updatePrimaryStatus(status: TimerStatus) {
        _timerState.value = _timerState.value.copy(
            primaryTimer = _timerState.value.primaryTimer.copy(status = status)
        )
    }

    private fun updateSubStatus(status: TimerStatus) {
        _timerState.value = _timerState.value.copy(
            subTimer = _timerState.value.subTimer.copy(status = status)
        )
    }
}