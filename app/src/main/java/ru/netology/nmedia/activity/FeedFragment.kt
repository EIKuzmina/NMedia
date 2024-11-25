package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.handler.*
import ru.netology.nmedia.model.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostFragment.Companion.idArg
import ru.netology.nmedia.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id, post.likedByMe)
            }

            override fun onRepost(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }

                val repostIntent = Intent.createChooser(
                    intent,
                    getString(R.string.chooser_repost_post)
                )
                startActivity(repostIntent)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
            }

            override fun onCardPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    Bundle().also { it.idArg = post.id }
                )
            }
        })

        binding.list.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swipeRefreshLayout.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { viewModel.loadPosts() }
                    .show()
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.emptyText.isVisible = state.empty
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadPosts()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }
}