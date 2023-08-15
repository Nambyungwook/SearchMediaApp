package com.nbw.searchmediaapp.data.repository

import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.model.VideosResponse
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    // Remote : API
    // Coroutine
    suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<ImagesResponse>

    suspend fun searchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<VideosResponse>

    // Rx
    fun rxSearchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<ImagesResponse>

    fun rxSearchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<VideosResponse>

    // Local : Room
    suspend fun insertMedia(media: Media)

    suspend fun deleteMedia(media: Media)

    fun getFavoriteMedias(): Flow<List<Media>>

    // DataStore
    suspend fun saveSortMode(mode: String)

    suspend fun getSortMode(): Flow<String>
}