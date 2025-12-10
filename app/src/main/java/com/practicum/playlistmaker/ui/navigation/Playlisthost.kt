package com.practicum.playlistmaker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.ui.screen.*
import com.practicum.playlistmaker.ui.presentation.PlaylistsViewModel
import com.practicum.playlistmaker.ui.presentation.SearchViewModel

@Composable
fun PlaylistHost(navController: NavHostController) {
    val playlistsViewModel: PlaylistsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.name
    ) {
        composable(Screen.Main.name) {
            MainScreen(
                onNavigateToSearch = { 
                    navController.navigate(Screen.Search.name) {
                        popUpTo(Screen.Main.name) { 
                            inclusive = false
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                onNavigateToSettings = { 
                    navController.navigate(Screen.Settings.name) {
                        popUpTo(Screen.Main.name) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToPlaylists = { 
                    navController.navigate(Screen.Playlists.name) {
                        popUpTo(Screen.Main.name) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToFavorites = { 
                    navController.navigate(Screen.Favorites.name) {
                        popUpTo(Screen.Main.name) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Search.name) {
            val vm: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = vm,
                onTrackClick = { track ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("track", track)
                    navController.navigate(Screen.TrackDetails.name) {
                        popUpTo(Screen.Search.name) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Settings.name) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Playlists.name) {
            PlaylistsScreen(
                playlistsViewModel = playlistsViewModel,
                addNewPlaylist = { navController.navigate(Screen.CreatePlaylist.name) },
                navigateToPlaylist = { playlistId ->
                    navController.navigate("${Screen.PlaylistDetails.name}/$playlistId")
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        
        composable("${Screen.PlaylistDetails.name}/{playlistId}") { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId")?.toLongOrNull() ?: 0L
            PlaylistScreen(
                playlistId = playlistId,
                playlistsViewModel = playlistsViewModel,
                navigateBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("track", track)
                    navController.navigate(Screen.TrackDetails.name) {
                        popUpTo(backStackEntry.destination.route ?: Screen.PlaylistDetails.name) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.CreatePlaylist.name) {
            CreatePlaylistScreen(
                playlistsViewModel = playlistsViewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.TrackDetails.name) {
            val track = navController.previousBackStackEntry?.savedStateHandle?.get<Track>("track")
            track?.let {
                TrackDetailsScreen(
                    track = it,
                    playlistsViewModel = playlistsViewModel,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Favorites.name) {
            FavoritesScreen(
                playlistsViewModel = playlistsViewModel,
                navigateBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("track", track)
                    navController.navigate(Screen.TrackDetails.name) {
                        popUpTo(Screen.Favorites.name) { inclusive = false }
                    }
                }
            )
        }
    }
}
