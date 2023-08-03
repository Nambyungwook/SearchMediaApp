package com.nbw.searchmediaapp.data.db

import androidx.room.TypeConverter
import com.nbw.searchmediaapp.data.model.Image
import com.nbw.searchmediaapp.data.model.MediaType
import com.nbw.searchmediaapp.data.model.Video
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class OrmConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun mediaTypeToString(value: MediaType): String {
        return value.name
    }

    @TypeConverter
    fun stringToMediaType(value: String): MediaType {
        return MediaType.valueOf(value)
    }

    @TypeConverter
    fun imageToString(value: Image?): String {
        val adapter = moshi.adapter(Image::class.java)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun stringToImage(value: String): Image? {
        val adapter = moshi.adapter(Image::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun videoToString(value: Video?): String {
        val adapter = moshi.adapter(Video::class.java)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun stringToVideo(value: String): Video? {
        val adapter = moshi.adapter(Video::class.java)
        return adapter.fromJson(value)
    }
}