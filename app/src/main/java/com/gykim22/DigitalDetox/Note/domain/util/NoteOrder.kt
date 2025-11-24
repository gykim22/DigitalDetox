package com.gykim22.DigitalDetox.Note.domain.util

/**
 * 노트의 정렬 기준을 정의합니다.
 * 기준은 제목, 생성 시각, 카테고리, 공부 시간, 휴식 시간, 전체 시간입니다.
 * @author Kim Giyun
 */
sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrder(orderType)
    class Timestamp(orderType: OrderType) : NoteOrder(orderType)
    class Category(orderType: OrderType) : NoteOrder(orderType)
    class StudyTime(orderType: OrderType) : NoteOrder(orderType)
    class BreakTime(orderType: OrderType) : NoteOrder(orderType)
    class TotalTime(orderType: OrderType) : NoteOrder(orderType)
}