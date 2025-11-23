package com.gykim22.DigitalDetox.Core

sealed class Screen {
    object NoteListScreen : Screen() {
        const val route = "note_list_screen"
    }

    object NoteEditScreen : Screen() {
        const val route = "note_edit_screen"
    }

    object TimerScreen : Screen() {
        const val route = "timer_screen"
    }
}