package com.gykim22.DigitalDetox.Note.domain.util

import androidx.compose.ui.focus.FocusState
import com.gykim22.DigitalDetox.Note.domain.model.Note

/**
 * 노트 정렬 및 삭제 이벤트를 정의합니다.
 * @author Kim Giyun
 */
sealed class NoteEvent {
    data class Order(val noteOrder: NoteOrder) : NoteEvent()
    data class DeleteNote(val note: Note) : NoteEvent()
}

/**
 * 노트 작성/수정 이벤트를 정의합니다.
 * @author Kim Giyun
 */
sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()

}