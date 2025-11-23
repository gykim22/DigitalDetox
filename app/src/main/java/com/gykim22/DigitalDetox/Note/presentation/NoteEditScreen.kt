package com.gykim22.DigitalDetox.Note.presentation

import android.R.attr.fontFamily
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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

    LaunchedEffect(key1 = true) {
        eventFlow.collect { event ->
            when (event) {
                is NoteViewModel.UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is NoteViewModel.UiEvent.SaveNote -> {
                    Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.NoteListScreen.route)
                }
            }
        }
    }
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
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
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
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        HintTextField(
            text = title,
            hint = titleState.hint,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
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
                fontSize = 20.sp
            )
        )
        HeightSpacer(10.dp)
        HintTextField(
            text = content,
            hint = contentState.hint,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onValueChange = {
                if (it.length <= 1000) onEvent(AddEditNoteEvent.EnteredContent(it))
            },
            onFocusChange = {
                onEvent(AddEditNoteEvent.ChangeContentFocus(it))
            },
            isHintVisible = isContentHintVisible,
            textStyle = TextStyle(
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
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