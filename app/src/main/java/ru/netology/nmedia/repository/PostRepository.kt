package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.handler.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun likeById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
}