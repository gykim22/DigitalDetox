package com.gykim22.DigitalDetox.Core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            TimerRoot(timerViewModel, navController)
        }
        composable(Screen.NoteEditScreen.route) {
            NoteEditRoot(noteViewModel, navController)
        }
        composable(
            route = "add_edit_note?total={total}&study={study}&rest={rest}",
            arguments = listOf(
                navArgument("total") { type = NavType.LongType; defaultValue = 0L },
                navArgument("study") { type = NavType.LongType; defaultValue = 0L },
                navArgument("rest") { type = NavType.LongType; defaultValue = 0L },
            )
        ) { entry ->
            val total = entry.arguments?.getLong("total") ?: 0L
            val study = entry.arguments?.getLong("study") ?: 0L
            val rest = entry.arguments?.getLong("rest") ?: 0L

            LaunchedEffect(Unit) {
                noteViewModel.setTimes(total, study, rest)
            }

            NoteEditRoot(noteViewModel, navController)
        }
    }
}