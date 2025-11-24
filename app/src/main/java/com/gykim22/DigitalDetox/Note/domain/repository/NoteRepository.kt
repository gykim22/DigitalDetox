package com.gykim22.DigitalDetox.Note.domain.repository

import com.gykim22.DigitalDetox.Note.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * 노트 레포지토리 인터페이스입니다.
 * @author Kim Giyun
 */
interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}