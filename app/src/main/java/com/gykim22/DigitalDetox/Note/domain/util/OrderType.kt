package com.gykim22.DigitalDetox.Note.domain.util

/**
 * 노트의 정렬 방식을 정의합니다.
 * @author Kim Giyun
 */
sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}