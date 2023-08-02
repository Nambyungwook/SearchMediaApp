package com.nbw.searchmediaapp.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideosResponse(
    @Json(name = "documents")
    val videos: List<Video>?,
    @Json(name = "meta")
    val meta: Meta?
)