package com.nbw.searchmediaapp.data.repository

import androidx.lifecycle.LiveData
import com.nbw.searchmediaapp.data.api.RetrofitInstance.api
import com.nbw.searchmediaapp.data.api.RetrofitInstance.rxApi
import com.nbw.searchmediaapp.data.db.MediaDatabase
import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.ResultWrapper
import com.nbw.searchmediaapp.data.model.VideosResponse
import com.nbw.searchmediaapp.utils.safeApiCall
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers

class MediaRepositoryImpl(
    private val db: MediaDatabase
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

    override fun getFavoriteMedias(): LiveData<List<Media>> {
        return db.mediaDao().getFavoriteMedias()
    }
}