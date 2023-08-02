package com.nbw.searchmediaapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbw.searchmediaapp.data.model.Media
import com.nbw.searchmediaapp.data.model.MediaType
import com.nbw.searchmediaapp.databinding.ItemMediaBinding

class MediaViewHolder(
    private val binding: ItemMediaBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(media: Media) {
        val title: String?
        val publisher: String?
        val date: String?
        val thumbnail: String?
        if (media.mediaType == MediaType.IMAGE) {
            title = media.image?.collection
            publisher = media.image?.displaySitename
            date = media.image?.datetime
            thumbnail = media.image?.thumbnailUrl
        } else if (media.mediaType == MediaType.VIDEO) {
            title = media.video?.title
            publisher = media.video?.author
            date = media.video?.datetime
            thumbnail = media.video?.thumbnail
        } else {
            title = "No Content"
            publisher = "No Publisher"
            date = "No Date"
            thumbnail = "No Thumbnail"
        }

        itemView.apply {
            Glide.with(this)
                .load(thumbnail)
                .centerCrop()
                .into(binding.ivMediaThumbnail)

            binding.tvMediaTitle.text = title
            binding.tvMeidaPublisher.text = publisher
            binding.tvDatetime.text = date
        }
    }
}