package com.gykim22.DigitalDetox.Note.data.repository

import com.gykim22.DigitalDetox.Note.data.data_source.NoteDao
import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getAllNotes() = dao.getAllNotes()

    override suspend fun getNoteById(id: Int) = dao.getNoteById(id)


    override suspend fun insertNote(note: Note) = dao.insertNote(note)


    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)
}