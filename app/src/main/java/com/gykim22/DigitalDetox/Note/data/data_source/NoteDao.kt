package com.gykim22.DigitalDetox.Note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gykim22.DigitalDetox.Note.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Note 테이블에 대한 데이터 액세스 객체입니다.
 * @author Kim Giyun
 */
@Dao
interface NoteDao {

    /* 모든 노트를 가져옵니다. */
    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    /* 해당하는 id의 노트를 가져옵니다. */
    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    /** 노트를 삽입합니다.
     * 이 때, REPLACE 정책은 충돌하는 기존 노트를 삭제하고, 새 노트를 삽입합니다.
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    /* 노트를 삭제합니다. */
    @Delete
    suspend fun deleteNote(note: Note)
}