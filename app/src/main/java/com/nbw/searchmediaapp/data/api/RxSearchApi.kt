package com.nbw.searchmediaapp.data.api

import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.VideosResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RxSearchApi {
    @GET("v2/search/image")
    fun rxSearchImages(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Observable<ImagesResponse>

    @GET("v2/search/vclip")
    fun rxSearchVideos(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Observable<VideosResponse>
}