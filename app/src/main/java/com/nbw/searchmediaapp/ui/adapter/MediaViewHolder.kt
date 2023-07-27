package com.nbw.searchmediaapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbw.searchmediaapp.data.model.Image
import com.nbw.searchmediaapp.databinding.ItemMediaBinding

class MediaViewHolder(
    private val binding: ItemMediaBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(media: Image) {
        val title = media.collection
        val site = media.displaySitename
        val date = media.datetime

        itemView.apply {
            Glide.with(this)
                .load(media.thumbnailUrl)
                .centerCrop()
                .into(binding.ivMediaThumbnail)

            binding.tvMediaTitle.text = title
            binding.tvMeidaSite.text = site
            binding.tvDatetime.text = date
        }
    }
}