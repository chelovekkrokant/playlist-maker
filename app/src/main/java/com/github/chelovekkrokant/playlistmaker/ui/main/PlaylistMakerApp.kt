package com.github.chelovekkrokant.playlistmaker.ui.main

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.chelovekkrokant.playlistmaker.di.databaseModule
import com.github.chelovekkrokant.playlistmaker.di.networkModule
import com.github.chelovekkrokant.playlistmaker.di.repositoryModule
import com.github.chelovekkrokant.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")

class PlaylistMakerApp : Application() {

    val dataStore: DataStore<Preferences>
        get() = appDataStore

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistMakerApp)
            modules(databaseModule, networkModule, repositoryModule, viewModelModule)
        }
    }
}