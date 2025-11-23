package com.gykim22.DigitalDetox.Core

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gykim22.DigitalDetox.Core.utils.BottomBar
import com.gykim22.DigitalDetox.Core.utils.BottomTab
import com.gykim22.DigitalDetox.Note.presentation.NoteEditRoot
import com.gykim22.DigitalDetox.Note.presentation.NoteListRoot
import com.gykim22.DigitalDetox.Note.presentation.NoteViewModel
import com.gykim22.DigitalDetox.Timer.presentation.TimerRoot
import com.gykim22.DigitalDetox.Timer.presentation.TimerViewModel

@Composable
fun MainGraph() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry)
        .value?.destination?.route

    val shouldShowBottomBar = when {
        currentRoute == Screen.NoteEditScreen.route -> false
        currentRoute?.startsWith("add_edit_note") == true -> false
        else -> true
    }

    val selectedTab = when (currentRoute) {
        Screen.TimerScreen.route -> BottomTab.Home
        Screen.NoteListScreen.route -> BottomTab.Calendar
        else -> BottomTab.Home
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        when (tab) {
                            BottomTab.Home -> {
                                navController.navigate(Screen.TimerScreen.route) {
                                    popUpTo(Screen.TimerScreen.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }

                            BottomTab.Calendar -> {
                                navController.navigate(Screen.NoteListScreen.route) {
                                    popUpTo(Screen.NoteListScreen.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }
        }
    ) {
        NavGraph(
            startDestination = Screen.TimerScreen.route,
            navController = navController
        )
    }
}

@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    val navController = navController
    val timerViewModel = hiltViewModel<TimerViewModel>()
    val noteViewModel = hiltViewModel<NoteViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.NoteListScreen.route) {
            NoteListRoot(noteViewModel, navController)
        }
        composable(Screen.TimerScreen.route) {
            TimerRoot(timerViewModel, navController)
        }
        composable(Screen.NoteEditScreen.route) {
            NoteEditRoot(noteViewModel, navController)
        }
        composable(
            route = "add_edit_note?noteId={noteId}&total={total}&study={study}&rest={rest}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType; defaultValue = -1 },
                navArgument("total") { type = NavType.LongType; defaultValue = 0L },
                navArgument("study") { type = NavType.LongType; defaultValue = 0L },
                navArgument("rest") { type = NavType.LongType; defaultValue = 0L },
            )
        ) { entry ->
            val noteId = entry.arguments?.getInt("noteId") ?: -1
            val total = entry.arguments?.getLong("total") ?: 0L
            val study = entry.arguments?.getLong("study") ?: 0L
            val rest = entry.arguments?.getLong("rest") ?: 0L

            LaunchedEffect(noteId) {
                if (noteId != -1) noteViewModel.loadNoteById(noteId)
                else noteViewModel.setTimes(total, study, rest)
            }

            NoteEditRoot(noteViewModel, navController)
        }
    }
}