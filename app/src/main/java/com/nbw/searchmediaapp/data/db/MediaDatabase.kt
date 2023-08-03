package com.nbw.searchmediaapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nbw.searchmediaapp.data.model.Media

@Database(
    entities = [Media::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class MediaDatabase: RoomDatabase() {

    abstract fun mediaDao(): MediaDao

    companion object {
        @Volatile
        private var INSTANCE: MediaDatabase? = null

        private fun buildDatabase(context: Context): MediaDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                MediaDatabase::class.java,
                "favortie-medias"
            ).build()

        fun getInstance(context: Context): MediaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}