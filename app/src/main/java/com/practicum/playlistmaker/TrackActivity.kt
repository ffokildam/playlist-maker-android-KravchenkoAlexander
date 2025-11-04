package com.practicum.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.practicum.playlistmaker.presentation.TrackViewModel

class TrackActivity : ComponentActivity() {
    private val viewModel: TrackViewModel by viewModels { TrackViewModel.getViewModelFactory("123") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Здесь можно добавить UI для тестирования ViewModel
    }
}

