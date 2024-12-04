package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.handler.Media
import ru.netology.nmedia.handler.MediaUpload
import ru.netology.nmedia.handler.Post

interface PostRepository {
    val data: Flow<List<Post>>

    fun getNewer(id: Int): Flow<Int>
    suspend fun getAll(show: Boolean = true)
    suspend fun likeById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
    suspend fun updateShow()
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, pass: String)
    suspend fun registration(author: String, login: String, password: String)
}