package com.nbw.searchmediaapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.MediaType
import com.nbw.searchmediaapp.databinding.ItemMediaBinding

class MediaAdapter: PagingDataAdapter<Media, MediaViewHolder>(MediaDiffCallback) {
    companion object {
        private val MediaDiffCallback = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                val oldItemIdentify = if (oldItem.mediaType == MediaType.IMAGE) {
                    oldItem.image?.imageUrl
                } else if (oldItem.mediaType == MediaType.VIDEO) {
                    oldItem.video?.url
                } else {
                    ""
                }

                val newItemIdentify = if (newItem.mediaType == MediaType.IMAGE) {
                    newItem.image?.imageUrl
                } else if (newItem.mediaType == MediaType.VIDEO) {
                    newItem.video?.url
                } else {
                    ""
                }

                return oldItemIdentify == newItemIdentify
            }

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem == newItem
            }

        }
    }

    private var onItemClickListener: ((Media) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val pagedMedia = getItem(position)
        pagedMedia?.let { media ->
            holder.bind(media)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(media) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Media) -> Unit) {
        onItemClickListener = listener
    }
}