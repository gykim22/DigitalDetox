package com.gykim22.DigitalDetox.Note.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gykim22.DigitalDetox.Core.Screen
import com.gykim22.DigitalDetox.Note.domain.util.AddEditNoteEvent
import com.gykim22.DigitalDetox.Note.presentation.components.HintTextField
import com.gykim22.DigitalDetox.R
import com.gykim22.DigitalDetox.Timer.presentation.util.CustomButton
import com.gykim22.DigitalDetox.Timer.presentation.util.HeightSpacer
import com.gykim22.DigitalDetox.ui.theme.blue100
import com.gykim22.DigitalDetox.ui.theme.pretendard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * 노트 작성/수정 화면입니다.
 * @param titleState 노트 제목 상태입니다. 제목 문자열과 작성 여부를 갖고 있습니다.
 * @param contentState 노트 내용 상태입니다. 내용 문자열과 작성 여부를 갖고 있습니다.
 * @param onEvent 노트 이벤트를 처리하는 람다입니다.
 * @param eventFlow 노트 이벤트 플로우입니다.
 * @param navController 네비게이션 컨트롤러입니다.
 * @author Kim Giyun
 */
@Composable
fun NoteEditScreen(
    titleState: NoteTextFieldState,
    contentState: NoteTextFieldState,
    onEvent: (AddEditNoteEvent) -> Unit,
    eventFlow: Flow<NoteViewModel.UiEvent>,
    navController: NavController
) {
    val title = titleState.text
    val content = contentState.text
    val isTitleHintVisible = titleState.isHintVisible
    val isContentHintVisible = contentState.isHintVisible
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    /**
     * 이벤트 플로우를 수집합니다.
     * 노트 저장 시 토스트 메시지를 띄우고 노트 리스트 화면으로 이동합니다.
     * @author Kim Giyun
     */
    LaunchedEffect(key1 = true) {
        eventFlow.collect { event ->
            when (event) {
                is NoteViewModel.UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is NoteViewModel.UiEvent.SaveNote -> {
                    Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                    navController.navigate(Screen.NoteListScreen.route) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    /**
     * 뒤로가기 버튼을 눌렀을 때 경고 다이얼로그를 띄웁니다.
     * @author Kim Giyun
     */
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "주의!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    fontFamily = pretendard
                )
            },
            text = {
                Text(
                    text = "지금 나가시면 학습 기록이 저장되지 않아요. 계속하시겠어요?",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    fontFamily = pretendard
                )
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_alert),
                    contentDescription = "경고",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            },
            containerColor = Color.White,
            titleContentColor = Color.Black,
            textContentColor = Color.Black,

            /**
             * 확인 버튼을 눌렀을 때 내용을 초기화하고 노트 리스트 화면으로 이동합니다.
             * @author Kim Giyun
             */
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onEvent(AddEditNoteEvent.EnteredTitle(""))
                    onEvent(AddEditNoteEvent.EnteredContent(""))
                    navController.popBackStack()
                }) {
                    Text(
                        text = "나가기",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        fontFamily = pretendard
                    )
                }
            },

            /**
             * 취소 버튼을 눌렀을 때 다이얼로그를 닫습니다.
             * @author Kim Giyun
             */
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(
                        text = "계속 작성하기",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        color = blue100
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        /* 노트 제목 */
        HintTextField(
            text = title,
            hint = titleState.hint,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                /* 노트 제목을 30자 이하로 제한합니다 */
                if (it.length <= 30) onEvent(AddEditNoteEvent.EnteredTitle(it))
            },
            onFocusChange = {
                onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
            },
            isHintVisible = isTitleHintVisible,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = if (isTitleHintVisible) Color.Gray else Color.Black
            )
        )

        HeightSpacer(10.dp)

        /* 노트 내용 */
        HintTextField(
            text = content,
            hint = contentState.hint,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onValueChange = {
                /* 노트 내용을 1000자 이하로 제한합니다. */
                if (it.length <= 1000) onEvent(AddEditNoteEvent.EnteredContent(it))
            },
            onFocusChange = {
                onEvent(AddEditNoteEvent.ChangeContentFocus(it))
            },
            isHintVisible = isContentHintVisible,
            textStyle = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = if (isContentHintVisible) Color.Gray else Color.Black
            )
        )

        HeightSpacer(10.dp)

        CustomButton(
            text = "저장",
            textColor = Color.White,
            enabled = title.isNotBlank() && content.isNotBlank(),
            mModifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                onEvent(AddEditNoteEvent.SaveNote)
            }
        )
    }
}

/**
 * 노트 작성/수정 Root 화면입니다.
 * ViewModel을 직접 주입하지 않기 위함입니다
 * @param viewModel 노트 뷰모델입니다.
 * @author Kim Giyun
 */
@Composable
fun NoteEditRoot(
    viewModel: NoteViewModel,
    navController: NavController
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    NoteEditScreen(
        titleState = titleState,
        contentState = contentState,
        onEvent = {
            viewModel.onAddEditNoteEvent(it)
        },
        eventFlow = viewModel.addEditEventFlow,
        navController = navController
    )
}

@Preview
@Composable
fun NoteEditScreenPreview() {
    NoteEditScreen(
        titleState = NoteTextFieldState(
            text = "Hello, World",
            hint = "제목을 입력해주세요.",
            isHintVisible = false
        ),
        contentState = NoteTextFieldState(
            text = "내용내용내용",
            hint = "내용을 입력해주세요.",
            isHintVisible = false
        ),
        onEvent = {},
        eventFlow = flowOf(),
        navController = NavController(LocalContext.current)
    )
}