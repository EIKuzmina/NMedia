package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.model.PostViewModel
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.util.StringArg

@AndroidEntryPoint
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
            findNavController().navigateUp()
        }

        viewModel.postCreated.observe(viewLifecycleOwner){
            findNavController().navigateUp()
        }
        return binding.root
    }
}