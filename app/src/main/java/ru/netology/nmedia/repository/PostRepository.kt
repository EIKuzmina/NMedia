package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.handler.Media
import ru.netology.nmedia.handler.MediaUpload
import ru.netology.nmedia.handler.Post

interface PostRepository {
    val data: Flow<PagingData<Post>>

    fun getNewer(id: Int): Flow<Int>
    suspend fun getAll(show: Boolean = true)
    suspend fun likeById(post: Post)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
    suspend fun updateShow()
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, pass: String)
    suspend fun registration(author: String, login: String, password: String)
}