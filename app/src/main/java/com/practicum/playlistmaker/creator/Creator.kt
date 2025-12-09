package com.practicum.playlistmaker.creator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.practicum.playlistmaker.data.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.database.AppDatabase
import com.practicum.playlistmaker.data.network.ITunesApiService
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.preferences.SearchHistoryPreferences
import com.practicum.playlistmaker.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.practicum.playlistmaker.ui.presentation.TrackPlayer
import com.practicum.playlistmaker.ui.presentation.TrackPlayerImpl
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history_preferences")

object Creator {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    private var appDatabase: AppDatabase? = null

    fun initDatabase(context: Context) {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getDatabase(context)
        }
    }

    private fun getDatabase(): AppDatabase {
        return appDatabase ?: throw IllegalStateException("Database not initialized. Call initDatabase first.")
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(iTunesApiService), getDatabase())
    }

    fun provideTracksRepository(): TracksRepository {
        return getTracksRepository()
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }

    fun provideTrackPlayer(): TrackPlayer {
        return TrackPlayerImpl()
    }

    fun provideAppDatabase(): AppDatabase {
        return getDatabase()
    }

    fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        val searchHistoryPreferences = SearchHistoryPreferences(context.dataStore)
        return SearchHistoryRepositoryImpl(searchHistoryPreferences)
    }

    fun providePlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(getDatabase())
    }
}