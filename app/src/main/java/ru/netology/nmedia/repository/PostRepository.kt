package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.handler.FeedItem
import ru.netology.nmedia.handler.Media
import ru.netology.nmedia.handler.MediaUpload
import ru.netology.nmedia.handler.Post

interface PostRepository {
    val appDb: AppDb
    val dataPaging: Flow<PagingData<FeedItem>>

    suspend fun likeById(post: Post)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
    suspend fun updateShow()
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, pass: String)
    suspend fun registration(author: String, login: String, password: String)
}