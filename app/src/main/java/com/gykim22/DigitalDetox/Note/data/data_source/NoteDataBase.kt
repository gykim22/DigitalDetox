package com.gykim22.DigitalDetox.Note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gykim22.DigitalDetox.Note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDataBase : RoomDatabase() {

    abstract val noteDao: NoteDao
}