package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.AndroidUtils
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostActivity.Companion.KEY_TEXT
import ru.netology.nmedia.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.requestFocus()
        binding.edit.setText(intent?.getStringExtra(Intent.EXTRA_TEXT))
        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, text)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}