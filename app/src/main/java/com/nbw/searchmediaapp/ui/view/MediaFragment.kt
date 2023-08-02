package com.nbw.searchmediaapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nbw.searchmediaapp.data.model.MediaType
import com.nbw.searchmediaapp.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private val args: MediaFragmentArgs by navArgs<MediaFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val media = args.media
        var mediaImage: String? = null
        var mediaTitle: String? = null
        var mediaDate: String? = null
        var mediaInfo: String? = null

        when (media.mediaType) {
            MediaType.IMAGE -> {
                media.image?.also {
                    mediaImage = it.imageUrl
                    mediaTitle = it.collection
                    mediaDate = it.datetime
                    mediaInfo = it.displaySitename
                }
            }
            MediaType.VIDEO -> {
                media.video?.also {
                    mediaImage = it.thumbnail
                    mediaTitle = it.title
                    mediaDate = it.datetime
                    mediaInfo = it.author + "\n" + it.playTime
                }
            }
            else -> { }
        }

        binding.apply {
            mediaImage?.let {
                Glide.with(requireContext())
                .load(it)
                .into(ivMedia)
            }
            tvMediaTitle.text = mediaTitle ?: "미디어 제목을 알 수 없습니다."
            tvMediaDate.text = mediaDate ?: "미디어 날짜를 알 수 없습니다."
            tvMediaInfo.text = mediaInfo ?: "미디어 관련 정보를 알 수 없습니다."

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}