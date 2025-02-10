package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.handler.*
import ru.netology.nmedia.model.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.MediaFragment.Companion.imageUrl
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostFragment.Companion.idArg
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.model.AuthViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {
    val viewModel: PostViewModel by activityViewModels()
    val viewModelAuth: AuthViewModel by activityViewModels()

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.like(post)
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
                if (!viewModelAuth.authenticated) {
                    findNavController().navigate(R.id.action_feedFragment_to_fragmentSignIn)
                    return
                }
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

            override fun onMedia(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_mediaFragment,
                    Bundle().also { it.imageUrl = post.attachment?.url }
                )
            }
        })

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PostLoadingStateAdapter { adapter.retry() },
            footer = PostLoadingStateAdapter { adapter.retry() }
        )
        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing =
                    it.refresh is LoadState.Loading
            }
        }

        binding.recentPosts.setOnClickListener {
            viewModel.updateShow()
            it.isVisible = false
            binding.list.smoothScrollToPosition(0)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        viewModelAuth.data.observe(viewLifecycleOwner) {
            adapter.refresh()
        }

        binding.add.setOnClickListener {
            if (!viewModelAuth.authenticated) {
                findNavController().navigate(R.id.action_feedFragment_to_fragmentSignIn)
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        return binding.root
    }
}