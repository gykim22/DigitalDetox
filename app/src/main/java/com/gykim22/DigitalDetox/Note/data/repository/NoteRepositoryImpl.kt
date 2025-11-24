package com.gykim22.DigitalDetox.Note.data.repository

import com.gykim22.DigitalDetox.Note.data.data_source.NoteDao
import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.domain.repository.NoteRepository

/**
 * 노트 레포지토리 구현체입니다.
 * @property dao 노트 데이터 액세스 객체입니다.
 * @author Kim Giyun
 */
class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getAllNotes() = dao.getAllNotes()

    override suspend fun getNoteById(id: Int) = dao.getNoteById(id)


    override suspend fun insertNote(note: Note) = dao.insertNote(note)


    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)
}