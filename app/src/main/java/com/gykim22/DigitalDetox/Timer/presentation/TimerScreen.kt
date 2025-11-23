package com.gykim22.DigitalDetox.Timer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.gykim22.DigitalDetox.Core.HandleBackPressToExitApp
import com.gykim22.DigitalDetox.Core.Screen
import com.gykim22.DigitalDetox.Timer.domain.model.Timer
import com.gykim22.DigitalDetox.Timer.domain.model.TimerStatus
import com.gykim22.DigitalDetox.Timer.presentation.util.CustomButton
import com.gykim22.DigitalDetox.Timer.presentation.util.adder
import com.gykim22.DigitalDetox.Timer.presentation.util.noRippleClickable
import com.gykim22.DigitalDetox.ui.theme.pretendard
import com.gykim22.DigitalDetox.ui.theme.red100
import java.util.concurrent.TimeUnit

@Composable
fun TimerScreen(
    primaryTimerState: Timer,
    subTimerState: Timer,
    onStartPause: () -> Unit,
    onStop: () -> Unit,
    navController: NavController
) {
    val formattedPrimaryTime = formatTime(primaryTimerState.timerSecond)
    val formattedSubTime = formatTime(subTimerState.timerSecond)

    /* 18시간 도달 시 자동 종료 */
    if (adder(formattedPrimaryTime, formattedSubTime)) onStop()

    HandleBackPressToExitApp()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formattedPrimaryTime,
            color = Color.White,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 70.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = formattedSubTime,
            color = Color.White,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(
                onClick = { onStartPause() },
                enabled = primaryTimerState.status != TimerStatus.RUNNING || primaryTimerState.timerSecond > 0L,
                mModifier = Modifier.weight(1f),
                text = when (primaryTimerState.status) {
                    TimerStatus.STOPPED -> "시작"
                    TimerStatus.RUNNING -> "일시정지"
                    TimerStatus.PAUSED -> "재개"
                }
            )

            CustomButton(
                onClick = { onStop() },
                buttonColor = red100,
                enabled = primaryTimerState.status != TimerStatus.STOPPED,
                mModifier = Modifier.weight(1f),
                text = "종료"
            )
        }
        Text("테스트", modifier = Modifier.noRippleClickable{
            navController.navigate(Screen.NoteListScreen.route)
        })
    }
}

@Preview
@Composable
fun TimerScreenPreview() {
    TimerScreen(
        primaryTimerState = Timer(
            timerSecond = 2000L,
            status = TimerStatus.RUNNING,
        ),
        subTimerState = Timer(
            timerSecond = 1000L,
            status = TimerStatus.RUNNING,
        ),
        onStartPause = {},
        onStop = {},
        navController = NavController(LocalContext.current)
    )
}

/**
 * TimerScreen에서 바로 TimerViewModel을 주입하지 않아 테스트 용이성을 확보했습니다.
 * @author Kim Giyun
 */
@Composable
fun TimerRoot(
    viewModel: TimerViewModel,
    navController: NavController
) {
    val state by viewModel.timerState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TimerUiEvent.NavigateToAddNote -> {
                    navController.navigate(
                        "add_edit_note" +
                                "?total=${event.total}" +
                                "&study=${event.study}" +
                                "&rest=${event.rest}"
                    )
                }
            }
        }
    }

    /**
     * 앱의 백그라운드, 포어그라운드 진입을 감지하여 타이머 동작을 제어하는 로직입니다.
     * 백그라운드 진입 -> PrimaryTimer 일시정지 / SubTimer 작동
     * 포어그라운드 진입 -> PrimaryTimer 작동 / SubTimer 일시 정지
     */
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> viewModel.onApplicationBackgroundTimer()
                Lifecycle.Event.ON_RESUME -> viewModel.onApplicationForegroundTimer()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    TimerScreen(
        primaryTimerState = state.primaryTimer,
        subTimerState = state.subTimer,
        onStartPause = { viewModel.startPauseTimer() },
        onStop = { viewModel.stopTimer() },
        navController = navController
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