package com.gykim22.DigitalDetox.Timer.presentation

import android.R.attr.bottom
import android.R.attr.enabled
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.gykim22.DigitalDetox.Timer.domain.model.Timer
import com.gykim22.DigitalDetox.Timer.domain.model.TimerStatus
import com.gykim22.DigitalDetox.Timer.presentation.util.CustomButton
import com.gykim22.DigitalDetox.ui.theme.pretendard
import java.util.concurrent.TimeUnit

@Composable
fun TimerScreen(
    state: Timer,
    onStartPause: () -> Unit,
    onStop: () -> Unit
) {
    val formattedTime = formatTime(state.timerSecond)

    /* 18시간 도달 시 자동 종료 */
    if (formattedTime == "18:00:00") onStop()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formattedTime,
            color = Color.White,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 70.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(
                onClick = { onStartPause() },
                enabled = state.status != TimerStatus.RUNNING || state.timerSecond > 0L,
                mModifier = Modifier.weight(1f),
                text = when (state.status) {
                    TimerStatus.STOPPED -> "시작"
                    TimerStatus.RUNNING -> "일시정지"
                    TimerStatus.PAUSED -> "재개"
                }
            )

            CustomButton(
                onClick = { onStartPause() },
                buttonColor = Color(0xFFEF524F),
                enabled = state.status != TimerStatus.STOPPED,
                mModifier = Modifier.weight(1f),
                text = "종료"
            )
        }
    }
}

@Preview
@Composable
fun TimerScreenPreview() {
    TimerScreen(
        state = Timer(
            timerSecond = 1000L,
            status = TimerStatus.RUNNING,
        ),
        onStartPause = {},
        onStop = {}
    )
}

/**
 * TimerScreen에서 바로 TimerViewModel을 주입하지 않아 테스트 용이성을 확보했습니다.
 */
@Composable
fun TimerScreenRoot() {
    val viewModel = hiltViewModel<TimerViewModel>()
    val state by viewModel.timerState.collectAsState()
    TimerScreen(
        state = state,
        onStartPause = { viewModel.startPauseTimer() },
        onStop = { viewModel.stopTimer() }
    )
}

/**
 * mills를 받아 HH:MM:SS 형식으로 변환하는 함수입니다.
 * @author Kim Giyun
 */
private fun formatTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

    /* 한 시간 미만일 시 MM:SS만 표기합니다. */
    if (hours == 0L)
        return String.format("%02d:%02d", minutes, seconds)
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}