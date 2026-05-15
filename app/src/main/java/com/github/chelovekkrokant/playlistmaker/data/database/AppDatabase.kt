package com.github.chelovekkrokant.playlistmaker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.chelovekkrokant.playlistmaker.data.database.dao.PlaylistsDao
import com.github.chelovekkrokant.playlistmaker.data.database.dao.TracksDao
import com.github.chelovekkrokant.playlistmaker.data.database.entity.PlaylistEntity
import com.github.chelovekkrokant.playlistmaker.data.database.entity.TrackEntity

@Database(
    entities = [TrackEntity::class, PlaylistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksDao(): TracksDao
    abstract fun playlistsDao(): PlaylistsDao
}
