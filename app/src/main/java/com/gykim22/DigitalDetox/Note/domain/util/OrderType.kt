package com.gykim22.DigitalDetox.Note.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}