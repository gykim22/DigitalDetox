package com.gykim22.DigitalDetox.Note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val contents: String,
    val category: String,
    val timestamp: Long,
    val total_time: Long,
    val study_time: Long,
    val break_time: Long,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
) {
    companion object {
        val categories = listOf("공부", "기타")
    }
}
