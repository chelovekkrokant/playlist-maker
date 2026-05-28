package com.github.chelovekkrokant.playlistmaker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.chelovekkrokant.playlistmaker.data.database.dao.PlaylistsDao
import com.github.chelovekkrokant.playlistmaker.data.database.dao.TracksDao
import com.github.chelovekkrokant.playlistmaker.data.database.entity.PlaylistEntity
import com.github.chelovekkrokant.playlistmaker.data.database.entity.TrackEntity

@Database(
    entities = [TrackEntity::class, PlaylistEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksDao(): TracksDao
    abstract fun playlistsDao(): PlaylistsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE playlists ADD COLUMN coverImageUri TEXT DEFAULT NULL")
            }
        }
    }
}
