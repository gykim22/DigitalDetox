package com.gykim22.DigitalDetox.Note.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.domain.use_cases.NoteUseCases
import com.gykim22.DigitalDetox.Note.domain.util.AddEditNoteEvent
import com.gykim22.DigitalDetox.Note.domain.util.NoteEvent
import com.gykim22.DigitalDetox.Note.domain.util.NoteOrder
import com.gykim22.DigitalDetox.Note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Timestamp(OrderType.Descending),
)

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _noteState = mutableStateOf(NoteState())
    val noteState: State<NoteState> = _noteState

    private var _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "제목을 입력해주세요."
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private var _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "내용을 입력해주세요."
        )
    )

    private var totalTime: Long = 0L
    private var studyTime: Long = 0L
    private var breakTime: Long = 0L
    private var timestamp: Long = 0L

    val noteContent: State<NoteTextFieldState> = _noteContent

    private var _addEditEventFlow = MutableSharedFlow<UiEvent>()
    val addEditEventFlow = _addEditEventFlow.asSharedFlow()

    var currentNoteId: Int? = null


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }

    private var job: Job? = null

    init {
        onNoteEvent(NoteEvent.Order(NoteOrder.Timestamp(OrderType.Descending)))
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.contents,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun setTimes(total: Long, study: Long, rest: Long) {
        totalTime = total
        studyTime = study
        breakTime = rest
    }

    fun onNoteEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.Order -> {
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

    fun onAddEditNoteEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                contents = noteContent.value.text,
                                category = "공부",
                                timestamp = currentNoteId?.let { timestamp }
                                    ?: System.currentTimeMillis(),
                                total_time = totalTime,
                                study_time = studyTime,
                                break_time = breakTime,
                                id = currentNoteId
                            )
                        )
                        _addEditEventFlow.emit(UiEvent.SaveNote)
                        resetNoteState()
                    } catch (e: Exception) {
                        _addEditEventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "저장에 실패했습니다."
                            )
                        )
                        return@launch
                    }
                }
            }

        }
    }

    fun loadNoteById(noteId: Int) {
        viewModelScope.launch {
            val note = noteUseCases.getNote(noteId)
            note?.let {
                _noteTitle.value = noteTitle.value.copy(
                    text = it.title,
                    isHintVisible = false
                )
                _noteContent.value = noteContent.value.copy(
                    text = it.contents,
                    isHintVisible = false
                )
                totalTime = it.total_time
                studyTime = it.study_time
                breakTime = it.break_time
                timestamp = it.timestamp
                currentNoteId = it.id
            }
        }
    }

    fun resetNoteState() {
        _noteTitle.value = NoteTextFieldState(
            text = "",
            hint = "제목을 입력해주세요.",
            isHintVisible = true
        )
        _noteContent.value = NoteTextFieldState(
            text = "",
            hint = "내용을 입력해주세요.",
            isHintVisible = true
        )

        totalTime = 0L
        studyTime = 0L
        breakTime = 0L
        timestamp = 0L
        currentNoteId = null
    }
}
