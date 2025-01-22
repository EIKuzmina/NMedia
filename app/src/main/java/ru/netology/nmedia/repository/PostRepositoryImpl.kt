package ru.netology.nmedia.repository

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import ru.netology.nmedia.api.Api
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.*
import ru.netology.nmedia.error.*
import ru.netology.nmedia.handler.*

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map(List<PostEntity>::toPost).flowOn(Dispatchers.Default)

    override suspend fun getAll(show: Boolean) {
        try {
            val response = Api.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map { it.copy(show = true) }.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewer(id: Int): Flow<Int> = flow {
        while (true) {
            delay(120_000)
            val response = Api.retrofitService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)

    override suspend fun likeById(id: Int) {
        val post = data.firstOrNull()?.find { it.id == id }
        post ?: return
        try {
            if (post.likedByMe) {
                dao.dislikeById(id)
                val response = Api.retrofitService.disLikeById(post.id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromPost(body))
            } else {
                dao.likeById(id)
                val response = Api.retrofitService.likeById(post.id)
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
            val response = Api.retrofitService.removeById(id)
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
            val response = Api.retrofitService.save(post)
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

    override suspend fun updateShow() {
        dao.show()
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            val postWthAttachment =
                post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWthAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )
            val response = Api.retrofitService.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun authentication(login: String, pass: String) {
        try {
            val response = Api.retrofitService.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { AppAuth.getInstance().setAuth(authState.id, it) }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
    override suspend fun registration(name: String, login: String, pass: String) {
        try {
            val response = Api.retrofitService.registrationUser(name, login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { AppAuth.getInstance().setAuth(authState.id, it) }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}