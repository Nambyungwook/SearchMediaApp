package com.nbw.searchmediaapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nbw.searchmediaapp.data.api.RetrofitInstance.api
import com.nbw.searchmediaapp.data.api.RetrofitInstance.rxApi
import com.nbw.searchmediaapp.data.db.MediaDatabase
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.model.VideosResponse
import com.nbw.searchmediaapp.data.repository.MediaRepositoryImpl.PreferencesKeys.SORT_MODE
import com.nbw.searchmediaapp.utils.Sort
import com.nbw.searchmediaapp.utils.safeApiCall
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

class MediaRepositoryImpl(
    private val db: MediaDatabase,
    private val dataStore: DataStore<Preferences>
): MediaRepository {
    override suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<ImagesResponse> {
        return safeApiCall(Dispatchers.IO) {
            api.searchImages(
                query,
                sort,
                page,
                size
            )
        }
    }

    override suspend fun searchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<VideosResponse> {
        return safeApiCall(Dispatchers.IO) {
            api.searchVideos(
                query,
                sort,
                page,
                size
            )
        }
    }

    override fun rxSearchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<ImagesResponse> {
        return rxApi.rxSearchImages(
            query,
            sort,
            page,
            size
        )
    }

    override fun rxSearchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<VideosResponse> {
        return rxApi.rxSearchVideos(
            query,
            sort,
            page,
            size
        )
    }

    // Room
    override suspend fun insertMedia(media: Media) {
        db.mediaDao().insertMedia(media)
    }

    override suspend fun deleteMedia(media: Media) {
        db.mediaDao().deleteMedia(media)
    }

    override fun getFavoriteMedias(): Flow<List<Media>> {
        return db.mediaDao().getFavoriteMedias()
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