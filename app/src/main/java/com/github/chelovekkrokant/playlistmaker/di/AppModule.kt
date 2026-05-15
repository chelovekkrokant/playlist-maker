package com.github.chelovekkrokant.playlistmaker.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.github.chelovekkrokant.playlistmaker.data.database.AppDatabase
import com.github.chelovekkrokant.playlistmaker.data.db.SearchHistoryRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.network.DomainTracksRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.network.ITunesApiService
import com.github.chelovekkrokant.playlistmaker.data.network.PlaylistsRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.NetworkClient
import com.github.chelovekkrokant.playlistmaker.data.network.RetrofitNetworkClient
import com.github.chelovekkrokant.playlistmaker.data.network.TracksRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.preferences.SearchHistoryPreferences
import com.github.chelovekkrokant.playlistmaker.data.repository.TracksRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.PlaylistsRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.SearchHistoryRepository
import com.github.chelovekkrokant.playlistmaker.domain.api.TracksRepository as DomainTracksRepository
import com.github.chelovekkrokant.playlistmaker.ui.main.PlaylistMakerApp
import com.github.chelovekkrokant.playlistmaker.ui.playlist.NewPlaylistViewModel
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistViewModel
import com.github.chelovekkrokant.playlistmaker.ui.playlist.PlaylistsViewModel
import com.github.chelovekkrokant.playlistmaker.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "playlists_maker"
        ).addMigrations(AppDatabase.MIGRATION_1_2).build()
    }
}

val networkModule = module {
    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
    single<NetworkClient> { RetrofitNetworkClient(get()) }
    single<TracksRepository> { TracksRepositoryImpl(get()) }
}

val repositoryModule = module {
    single<DataStore<Preferences>> { (androidApplication() as PlaylistMakerApp).dataStore }
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    single<DomainTracksRepository> { DomainTracksRepositoryImpl(get()) }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(SearchHistoryPreferences(get()))
    }
}

val viewModelModule = module {
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { (playlistId: Long) -> PlaylistViewModel(playlistId, get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { NewPlaylistViewModel(get()) }
}