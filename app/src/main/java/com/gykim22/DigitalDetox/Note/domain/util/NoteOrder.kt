package com.gykim22.DigitalDetox.Note.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrder(orderType)
    class Timestamp(orderType: OrderType) : NoteOrder(orderType)
    class Category(orderType: OrderType) : NoteOrder(orderType)
    class StudyTime(orderType: OrderType) : NoteOrder(orderType)
    class BreakTime(orderType: OrderType) : NoteOrder(orderType)
    class TotalTime(orderType: OrderType) : NoteOrder(orderType)
}