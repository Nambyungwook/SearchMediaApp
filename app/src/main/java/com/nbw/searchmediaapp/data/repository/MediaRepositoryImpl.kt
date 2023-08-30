package com.nbw.searchmediaapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nbw.searchmediaapp.data.db.MediaDatabase
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.repository.MediaRepositoryImpl.PreferencesKeys.SORT_MODE
import com.nbw.searchmediaapp.utils.Constants.PAGING_SIZE
import com.nbw.searchmediaapp.utils.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

class MediaRepositoryImpl(
    private val db: MediaDatabase,
    private val dataStore: DataStore<Preferences>
): MediaRepository {
    override suspend fun searchMedias(
        query: String,
        sort: String
    ): Flow<PagingData<Media>> {
        val pagingSourceFactory = { MediaPagingSource(query, sort) }

        return Pager(
                config = PagingConfig(
                    pageSize = PAGING_SIZE,
                    enablePlaceholders = false,
                    maxSize = PAGING_SIZE * 3
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow
    }

    // Room
    override suspend fun insertMedia(media: Media) {
        db.mediaDao().insertMedia(media)
    }

    override suspend fun deleteMedia(media: Media) {
        db.mediaDao().deleteMedia(media)
    }

    override fun getFavoriteMedias(): Flow<PagingData<Media>> {
        val pagingSourceFactory = { db.mediaDao().getFavoriteMedias() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    // DataStore
    private object PreferencesKeys {
        val SORT_MODE = stringPreferencesKey("sort_mode")
    }

    override suspend fun saveSortMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode
        }
    }

    override suspend fun getSortMode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value
            }
    }
}