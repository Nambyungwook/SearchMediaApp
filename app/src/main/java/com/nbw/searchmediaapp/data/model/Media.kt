package com.nbw.searchmediaapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "medias")
data class Media(
    @PrimaryKey(autoGenerate = true)
    var mediaId: Long = 0,
    @ColumnInfo(name = "mediaType")
    val mediaType: MediaType,
    @ColumnInfo(name = "image")
    val image: Image?,
    @ColumnInfo(name = "video")
    val video: Video?
): Parcelable
