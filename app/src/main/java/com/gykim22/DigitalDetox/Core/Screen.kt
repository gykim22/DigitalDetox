package com.gykim22.DigitalDetox.Core

/**
 * 앱 내 모든 화면을 정의합니다.
 * @author Kim Giyun
 */
sealed class Screen(val route: String) {
    object NoteListScreen : Screen("note_list_screen")
    object NoteEditScreen : Screen("note_edit_screen")
    object TimerScreen : Screen("timer_screen")
}