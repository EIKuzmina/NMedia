package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun repost(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
    fun videoById()
}