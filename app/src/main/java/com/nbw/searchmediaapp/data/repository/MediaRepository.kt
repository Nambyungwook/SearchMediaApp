package com.nbw.searchmediaapp.data.repository

import androidx.paging.PagingData
import com.nbw.searchmediaapp.data.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    // Remote : API
    // Coroutine
    suspend fun searchMedias(
        query: String,
        sort: String
    ): Flow<PagingData<Media>>

    // Local : Room
    suspend fun insertMedia(media: Media)

    suspend fun deleteMedia(media: Media)

    fun getFavoriteMedias(): Flow<PagingData<Media>>

    // DataStore
    suspend fun saveSortMode(mode: String)

    suspend fun getSortMode(): Flow<String>
}