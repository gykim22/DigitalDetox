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

/**
 * 모든 노트를 가져오는 UseCase입니다.
 * @property repository 노트 레포지토리입니다.
 * @author Kim Giyun
 */
class GetAllNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Timestamp(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getAllNotes().map { notes ->
            /**
             * 노트의 정렬 기준에 따라 정렬합니다.
             * @param noteOrder 노트의 정렬 기준입니다.
             * @return 정렬된 노트 리스트입니다.
             */
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

/**
 * 노트를 삭제하는 UseCase입니다.
 * @author Kim Giyun
 */
class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}

/**
 * 노트를 추가하는 UseCase입니다.
 * @author Kim Giyun
 */
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

/**
 * id를 활용해 노트를 가져오는 UseCase입니다.
 * @author Kim Giyun
 */
class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}