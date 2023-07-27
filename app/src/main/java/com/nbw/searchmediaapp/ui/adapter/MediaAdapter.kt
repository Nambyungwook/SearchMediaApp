package com.nbw.searchmediaapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nbw.searchmediaapp.data.model.Image
import com.nbw.searchmediaapp.databinding.ItemMediaBinding

class MediaAdapter: ListAdapter<Image, MediaViewHolder>(MediaDiffCallback) {
    companion object {
        private val MediaDiffCallback = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = currentList[position]
        holder.bind(media)
    }
}