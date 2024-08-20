package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.OnInteractionListener
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostViewHolder
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
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
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId = arguments?.idArg

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val postFragment = posts.firstOrNull { it.id == postId } ?: return@observe
            val viewHolder = PostViewHolder(binding.fragmentPost, object : OnInteractionListener {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onRepost(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    viewModel.repost(post.id)
                    val repostIntent = Intent.createChooser(
                        intent,
                        getString(R.string.chooser_repost_post)
                    )
                    startActivity(repostIntent)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                    findNavController().navigateUp()
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_postFragment_to_editPostFragment,
                        Bundle().apply { textArg = post.content }
                    )
                }

                override fun onVideo(post: Post) {
                    viewModel.videoById()
                    if (post.video.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@PostFragment.context,
                            "Content can't be empty",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                }

                override fun onCardPost(post: Post) {
                }

            }).bind(postFragment)
        }
        return binding.root
    }
}