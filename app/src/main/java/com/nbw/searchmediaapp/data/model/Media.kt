package com.nbw.searchmediaapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val mediaType: MediaType,
    val image: Image?,
    val video: Video?
): Parcelable
