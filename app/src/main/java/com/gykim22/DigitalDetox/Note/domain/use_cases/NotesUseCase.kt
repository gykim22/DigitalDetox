package com.gykim22.DigitalDetox.Note.domain.use_cases

import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.domain.repository.NoteRepository
import com.gykim22.DigitalDetox.Note.domain.util.NoteOrder
import com.gykim22.DigitalDetox.Note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class NoteUseCases(
    val getAllNotes: GetAllNotesUseCase,
    val deleteNote: DeleteNoteUseCase,
    val addNote: AddNoteUseCase,
    val getNote: GetNoteUseCase
)

class GetAllNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Timestamp(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getAllNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Timestamp -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Category -> notes.sortedBy { it.category }
                        is NoteOrder.StudyTime -> notes.sortedBy { it.study_time }
                        is NoteOrder.BreakTime -> notes.sortedBy { it.break_time }
                        is NoteOrder.TotalTime -> notes.sortedBy { it.total_time }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Timestamp -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Category -> notes.sortedByDescending { it.category }
                        is NoteOrder.StudyTime -> notes.sortedByDescending { it.study_time }
                        is NoteOrder.BreakTime -> notes.sortedByDescending { it.break_time }
                        is NoteOrder.TotalTime -> notes.sortedByDescending { it.total_time }
                    }
                }
            }
        }
    }
}

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw Exception("제목을 한 글자 이상 입력해주세요.")
        }

        if (note.contents.isBlank()) {
            throw Exception("내용을 한 글자 이상 입력해주세요.")
        }

        repository.insertNote(note)
    }
}

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}