package com.gykim22.DigitalDetox.Core

sealed class Screen(val route: String) {
    object NoteListScreen : Screen("note_list_screen")
    object NoteEditScreen : Screen("note_edit_screen")
    object TimerScreen : Screen("timer_screen")
}