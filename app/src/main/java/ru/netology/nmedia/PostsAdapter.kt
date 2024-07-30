package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    class PostsAdapter(
        private val onLikeListener: OnLikeListener,
        private val onShareListener: OnShareListener
    ) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val binding =
                CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PostViewHolder(binding, onLikeListener, onShareListener)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            val post = getItem(position)
            holder.bind(post)
        }
    }

    class PostViewHolder(
        private val binding: CardPostBinding,
        private val onLikeListener: OnLikeListener,
        private val onShareListener: OnShareListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                author.text = post.author
                published.text = post.published
                base.text = post.content
                view.text = formatNumber(post.vieww)
                like.text = formatNumber(post.likeCount)
                repost.text = formatNumber(post.share)

                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24
                    else R.drawable.baseline_favorite_border_24
                )

                likes.setOnClickListener {
                    onLikeListener(post)
                }
                reposts.setImageResource(
                    if (post.shareByMe) R.drawable.baseline_share_24
                    else R.drawable.baseline_share_24
                )
                reposts.setOnClickListener {
                    onShareListener(post)
                }
                views.setImageResource(
                    R.drawable.baseline_remove_red_eye_24
                )
            }
        }
    }
}
