package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.PostArg

class PostFragment : Fragment() {
    companion object {
        var Bundle.idArg: Int by PostArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }
}