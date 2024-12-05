package ru.netology.nmedia.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.*
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.model.PostViewModel
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.*

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg by StringArg
    }

    private var fragmentBinding: FragmentNewPostBinding? = null
    private var menuProvider: MenuProvider? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        binding.editSave.requestFocus()
        arguments?.textArg?.let(binding.editSave::setText)
        fragmentBinding = binding

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.new_post, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.save -> {
                        fragmentBinding?.let {
                            viewModel.changeContent(it.editSave.text.toString())
                            viewModel.save()
                            AndroidUtils.hideKeyboard(requireView())
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        menuProvider?.let {
            requireActivity().addMenuProvider(it)
        }

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        viewModel.clearPhoto(uri, uri?.toFile())
                    }
                }
            }

        binding.removePhoto.setOnClickListener {
            viewModel.clearPhoto(null, null)
        }

        viewModel.photo.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.photoContainer.isGone = true
                return@observe
            }
            binding.photoContainer.isVisible = true
            binding.preview.setImageURI(it.uri)
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }
        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(arrayOf("image/*"))
                .createIntent(pickPhotoLauncher::launch)
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
    override fun onDestroyView() {
        fragmentBinding = null
        menuProvider?.let {
            requireActivity().removeMenuProvider(it)
        }
        super.onDestroyView()
    }
}