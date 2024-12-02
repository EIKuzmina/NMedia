package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.FragmentMediaBinding
import ru.netology.nmedia.handler.*
import ru.netology.nmedia.util.StringArg

class MediaFragment: Fragment() {
    companion object {
        var Bundle.imageUrl: String? by StringArg
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMediaBinding.inflate(inflater, container, false)
        arguments?.imageUrl.let {
            binding.media.load("${BuildConfig.BASE_URL}/media/")
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
        return binding.root
    }
}