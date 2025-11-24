package com.gykim22.DigitalDetox.Note.presentation.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun dateFormat(milliseconds: Long): String {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(milliseconds),
        ZoneOffset.systemDefault()
    ).format(DateTimeFormatter.ISO_DATE)
}
