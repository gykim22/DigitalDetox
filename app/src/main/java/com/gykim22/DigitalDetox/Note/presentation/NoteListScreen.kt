package com.gykim22.DigitalDetox.Note.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.domain.util.NoteEvent
import com.gykim22.DigitalDetox.Note.presentation.components.NoteItem
import com.gykim22.DigitalDetox.Note.presentation.components.NoteOrderSection
import com.gykim22.DigitalDetox.Timer.presentation.util.HeightSpacer

@Composable
fun NoteListScreen(
    noteState: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        NoteOrderSection(
            currentOrder = noteState.noteOrder,
            onOrderChange = {
                onEvent(NoteEvent.Order(it))
            }
        )
        HeightSpacer(10.dp)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(noteState.notes) { note ->
                NoteItem(
                    note = note,
                    onDeleteClick = {
                        onEvent(NoteEvent.DeleteNote(note))
                    },
                    onEditClick = {

                    }
                )
            }
        }
    }
}

@Composable
fun NoteListRoot(
    viewModel: NoteViewModel
) {
    val state by viewModel.noteState
    NoteListScreen(
        noteState = state,
        onEvent = {
            viewModel.onNoteEvent(it)
        }
    )
}

@Preview
@Composable
fun NoteListScreenPreview() {
    NoteListScreen(
        noteState = NoteState(
            notes = listOf(
                Note(
                    title = "제목제목제목제목",
                    contents = "내용내용내용내용내용내용",
                    category = "공부",
                    timestamp = 1763941200000,
                    total_time = 300,
                    study_time = 200,
                    break_time = 100
                ),
                Note(
                    title = "제목제목제목제목",
                    contents = "내용내용내용",
                    category = "공부",
                    timestamp = 1760941200000,
                    total_time = 500,
                    study_time = 150,
                    break_time = 350
                )
            )
        ),
        onEvent = {}
    )
}