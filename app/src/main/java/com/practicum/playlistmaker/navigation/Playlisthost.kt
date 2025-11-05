package com.practicum.playlistmaker.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.playlistmaker.MainScreen
import com.practicum.playlistmaker.SearchScreen
import com.practicum.playlistmaker.SettingsScreen
import com.practicum.playlistmaker.presentation.SearchViewModel

@Composable
fun PlaylistHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Main.name
    ) {
        composable(Screen.Main.name) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search.name) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.name) }
            )
        }

        composable(Screen.Search.name) {
            val vm: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = vm,
            )
        }

        composable(Screen.Settings.name) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
