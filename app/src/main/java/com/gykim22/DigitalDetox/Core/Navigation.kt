package com.gykim22.DigitalDetox.Core

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gykim22.DigitalDetox.Note.presentation.NoteEditRoot
import com.gykim22.DigitalDetox.Note.presentation.NoteListRoot
import com.gykim22.DigitalDetox.Note.presentation.NoteViewModel
import com.gykim22.DigitalDetox.Timer.presentation.TimerRoot
import com.gykim22.DigitalDetox.Timer.presentation.TimerViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()
    val timerViewModel = hiltViewModel<TimerViewModel>()
    val noteViewModel = hiltViewModel<NoteViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListRoot(noteViewModel)
        }
        composable(Screen.TimerScreen.route) {
            TimerRoot(timerViewModel)
        }
        composable(Screen.NoteEditScreen.route) {
            NoteEditRoot(noteViewModel)
        }

    }
}