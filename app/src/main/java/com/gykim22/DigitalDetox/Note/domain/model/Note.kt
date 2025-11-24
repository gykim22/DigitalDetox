package com.gykim22.DigitalDetox.Note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 노트 엔티티입니다.
 * @property title 노트의 제목입니다.
 * @property contents 노트의 내용입니다.
 * @property category 노트의 카테고리입니다.
 * @property timestamp 노트가 생성된 시간입니다.
 * @property total_time 전체 시간입니다.
 * @property study_time 공부 시간입니다.
 * @property break_time 휴식 시간입니다.
 * @property id 노트의 Primary Key id입니다. 자동 생성됩니다.
 * @author Kim Giyun
 */
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
