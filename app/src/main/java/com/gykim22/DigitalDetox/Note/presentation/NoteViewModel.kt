package com.gykim22.DigitalDetox.Note.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.domain.use_cases.NoteUseCases
import com.gykim22.DigitalDetox.Note.domain.util.NoteEvent
import com.gykim22.DigitalDetox.Note.domain.util.NoteOrder
import com.gykim22.DigitalDetox.Note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Timestamp(OrderType.Descending),
)


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    private var _noteState = mutableStateOf(NoteState())
    val noteState: State<NoteState> = _noteState

    private var job: Job? = null

    init {
        onNoteEvent(NoteEvent.Order(NoteOrder.Timestamp(OrderType.Descending)))
    }

    fun onNoteEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.Order -> {
                if (noteState.value.noteOrder::class == event.noteOrder::class &&
                    noteState.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                job?.cancel()
                job = noteUseCases.getAllNotes(event.noteOrder)
                    .onEach { notes ->
                        _noteState.value = noteState.value.copy(
                            notes = notes,
                            noteOrder = event.noteOrder
                        )
                    }.launchIn(viewModelScope)


            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                }
            }
        }
    }
}
