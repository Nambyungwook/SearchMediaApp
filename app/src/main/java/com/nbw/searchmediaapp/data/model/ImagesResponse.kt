package com.nbw.searchmediaapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesResponse(
    @Json(name = "documents")
    val images: List<Image>?,
    @Json(name = "meta")
    val meta: Meta?
)