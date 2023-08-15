package com.nbw.searchmediaapp.utils

import com.nbw.searchmediaapp.BuildConfig

object Constants {
    const val BASE_URL = "https://dapi.kakao.com/"

    const val API_KEY = BuildConfig.apiKey

    const val SEARCH_TIME_DELAY = 100L

    const val DATASTORE_NAME = "preferences_datastore"
}