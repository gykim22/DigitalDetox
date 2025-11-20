package com.gykim22.DigitalDetox.di

import android.app.Application
import androidx.room.Room
import com.gykim22.DigitalDetox.Note.data.data_source.NoteDataBase
import com.gykim22.DigitalDetox.Note.data.repository.NoteRepositoryImpl
import com.gykim22.DigitalDetox.Note.domain.repository.NoteRepository
import com.gykim22.DigitalDetox.Note.domain.use_cases.AddNoteUseCase
import com.gykim22.DigitalDetox.Note.domain.use_cases.DeleteNoteUseCase
import com.gykim22.DigitalDetox.Note.domain.use_cases.GetAllNotesUseCase
import com.gykim22.DigitalDetox.Note.domain.use_cases.GetNoteUseCase
import com.gykim22.DigitalDetox.Note.domain.use_cases.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDataBase {
        return Room.databaseBuilder(
            app,
            NoteDataBase::class.java,
            "note_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDataBase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotes = GetAllNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            addNote = AddNoteUseCase(repository),
            getNote = GetNoteUseCase(repository)
        )
    }
}