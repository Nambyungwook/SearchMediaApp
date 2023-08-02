package com.nbw.searchmediaapp.data.repository

import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.model.VideosResponse
import io.reactivex.Observable

interface MediaRepository {
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
}