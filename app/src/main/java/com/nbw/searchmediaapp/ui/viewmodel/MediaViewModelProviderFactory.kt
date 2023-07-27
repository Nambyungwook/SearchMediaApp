package com.nbw.searchmediaapp.ui.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.nbw.searchmediaapp.data.repository.MediaRepository

@Suppress("UNCHECKED_CAST")
class MediaViewModelProviderFactory(
    private val mediaRepository: MediaRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
): AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            return MediaViewModel(mediaRepository, handle) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}