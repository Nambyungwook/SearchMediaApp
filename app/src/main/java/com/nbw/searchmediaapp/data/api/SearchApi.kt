package com.nbw.searchmediaapp.data.api


import com.nbw.searchmediaapp.data.model.ImagesResponse
import com.nbw.searchmediaapp.data.model.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("v2/search/image")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ImagesResponse

    @GET("v2/search/vclip")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): VideosResponse
}