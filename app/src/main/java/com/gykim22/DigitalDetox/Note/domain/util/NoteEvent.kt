package com.gykim22.DigitalDetox.Note.domain.util

import com.gykim22.DigitalDetox.Note.domain.model.Note

sealed class NoteEvent {
    data class Order(val noteOrder: NoteOrder) : NoteEvent()
    data class DeleteNote(val note: Note) : NoteEvent()
}