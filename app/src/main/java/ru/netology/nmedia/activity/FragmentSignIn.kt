package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.model.AuthViewModel
import ru.netology.nmedia.util.AndroidUtils

class FragmentSignIn : Fragment() {
    private val viewModelAuth: AuthViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.butSignin.setOnClickListener {
            val login = binding.login.text?.trim().toString()
            val pass = binding.password.text?.trim().toString()
            viewModelAuth.authentication(login, pass)
            AndroidUtils.hideKeyboard(it)
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
        return binding.root
    }
}
