package ru.netology.nmedia.repository

import androidx.lifecycle.*
import okio.IOException
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.*
import ru.netology.nmedia.error.*
import ru.netology.nmedia.handler.Post

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map(List<PostEntity>::toPost)

    override suspend fun getAll() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Int) {
        val post = data.value?.find { it.id == id }
        post ?: return
        try {
            if (post.likedByMe) {
                dao.dislikeById(id)
                val response = PostsApi.retrofitService.disLikeById(post.id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromPost(body))
            } else {
                dao.likeById(id)
                val response = PostsApi.retrofitService.likeById(post.id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromPost(body))
            }
        } catch (e: IOException) {
            dao.insert(PostEntity.fromPost(post))
            throw NetworkError
        } catch (e: Exception) {
            dao.insert(PostEntity.fromPost(post))
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Int) {
        dao.removeById(id)
        try {
            val response = PostsApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromPost(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}