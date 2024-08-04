package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRepost(post: Post) {
                viewModel.repost(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val startList = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (startList) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) {
            with(binding.content) {
                if (it.id != 0) {
                    binding.group.visibility = View.VISIBLE
                    requestFocus()
                    setText(it.content)
                }
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Content can't be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                binding.group.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)
            }
        }
        binding.content.setOnClickListener {
            binding.group.visibility = View.VISIBLE
        }

        binding.cancel.setOnClickListener {
            with(binding.content) {
                viewModel.clearEdit()
                setText("")
                clearFocus()
                binding.group.visibility = View.GONE
                AndroidUtils.hideKeyboard(this)
            }
        }
    }
}
