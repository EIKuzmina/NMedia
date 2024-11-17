package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.model.PostViewModel
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg by StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg?.let(binding.editSave::setText)

        binding.okSave.setOnClickListener {
            val text = binding.editSave.text.toString()
            if (text.isNotBlank()) {
                viewModel.changeContent(text)
                viewModel.save()
                    viewModel.draftContent("")
                AndroidUtils.hideKeyboard(requireView())
            }
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.retrofitError.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(),
                "${resources.getString(R.string.error)} $it",
                Snackbar.LENGTH_LONG).show()
        })

        return binding.root
    }
}