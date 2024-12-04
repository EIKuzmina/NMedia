package ru.netology.nmedia.handler

import android.view.*
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import com.google.gson.Gson
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.util.formatNumber

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onRepost(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onCardPost(post: Post) {}
    fun onMedia(post: Post) {}
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    private val gson = Gson()
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            avatar.loadAvatar("${BASE_URL}/avatars/${post.authorAvatar}")
            published.text = post.published
            base.text = post.content
            image.let {
                if (post.attachment != null  && post.attachment.type == AttachmentType.IMAGE) {
                    it.visibility = View.VISIBLE
                    it.load("${BASE_URL}/media/${post.attachment.url}")
                } else it.visibility = View.GONE
            }
            likes.isChecked = post.likedByMe
            likes.text = formatNumber(post.likes)
            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            cardPost.setOnClickListener {
                onInteractionListener.onCardPost(post)
            }

            image.setOnClickListener {
                onInteractionListener.onMedia(post)
            }

            menu.isVisible = post.ownedByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}