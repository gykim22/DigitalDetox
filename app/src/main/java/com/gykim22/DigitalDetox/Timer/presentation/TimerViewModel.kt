package com.gykim22.DigitalDetox.Timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gykim22.DigitalDetox.Timer.domain.model.Timer
import com.gykim22.DigitalDetox.Timer.domain.model.TimerStatus
import com.gykim22.DigitalDetox.Timer.domain.repository.TimerRepository
import com.gykim22.DigitalDetox.Timer.domain.use_cases.TimerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerUseCases: TimerUseCases,
    private val repository: TimerRepository
) : ViewModel() {
    private val _timerState = MutableStateFlow(Timer())
    val timerState: StateFlow<Timer> = _timerState.asStateFlow()

    init {
        collectTimerState()
    }

    private fun collectTimerState() {
        viewModelScope.launch {
            timerUseCases().collect { mills ->
                _timerState.value = _timerState.value.copy(
                    timerSecond = mills
                )
            }
        }
    }

    fun startPauseTimer(){
        when (_timerState.value.status) {
            TimerStatus.RUNNING -> {
                repository.pauseTimer()
                _timerState.value = _timerState.value.copy(status = TimerStatus.PAUSED)

            }
            TimerStatus.PAUSED, TimerStatus.STOPPED -> {
                repository.startTimer()
                _timerState.value = _timerState.value.copy(status = TimerStatus.RUNNING)
            }
        }
    }

    fun stopTimer(){
        repository.stopTimer()
        _timerState.value = _timerState.value.copy(status = TimerStatus.STOPPED)
    }
}