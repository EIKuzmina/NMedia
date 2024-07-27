package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
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
                reposts.setImageResource(
                    if (post.shareByMe) R.drawable.baseline_share_24
                    else R.drawable.baseline_share_24
                )
                views.setImageResource(
                    R.drawable.baseline_remove_red_eye_24
                )
            }
        }

        binding.likes.setOnClickListener {
            viewModel.like()
        }
        binding.reposts.setOnClickListener {
            viewModel.repost()
        }
    }
}