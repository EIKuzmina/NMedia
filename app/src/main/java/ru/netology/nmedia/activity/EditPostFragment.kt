package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg

class EditPostFragment : Fragment() {
    companion object {
        var Bundle.textArg by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        binding.edit.requestFocus()
        arguments?.textArg?.let(binding.edit::setText)
        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            viewModel.changeContent(text)
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            viewModel.clearEdit()
            findNavController().navigateUp()
        }
        return binding.root
    }
}