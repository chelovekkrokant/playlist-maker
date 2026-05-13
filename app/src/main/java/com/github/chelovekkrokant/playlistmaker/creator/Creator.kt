package com.github.chelovekkrokant.playlistmaker.creator

import com.github.chelovekkrokant.playlistmaker.data.network.ITunesApiService
import com.github.chelovekkrokant.playlistmaker.data.network.RetrofitNetworkClient
import com.github.chelovekkrokant.playlistmaker.data.network.TracksRepositoryImpl
import com.github.chelovekkrokant.playlistmaker.data.repository.TracksRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    fun getTracksRepository(): TracksRepository =
        TracksRepositoryImpl(RetrofitNetworkClient(api))
}
