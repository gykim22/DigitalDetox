package com.gykim22.DigitalDetox.Note.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gykim22.DigitalDetox.Core.Screen
import com.gykim22.DigitalDetox.Note.domain.util.AddEditNoteEvent
import com.gykim22.DigitalDetox.Note.presentation.components.HintTextField
import com.gykim22.DigitalDetox.Timer.presentation.util.CustomButton
import com.gykim22.DigitalDetox.Timer.presentation.util.HeightSpacer
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
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
            text = "",
            hint = "내용을 입력해주세요.",
            isHintVisible = true
        ),
        onEvent = {},
        eventFlow = flowOf(),
        navController = NavController(LocalContext.current)
    )
}