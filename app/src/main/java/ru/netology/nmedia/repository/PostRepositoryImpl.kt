package ru.netology.nmedia.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.*
import androidx.paging.map
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dao.*
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.entity.*
import ru.netology.nmedia.error.*
import ru.netology.nmedia.handler.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: ApiService,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    override val appDb: AppDb,
    private val auth: AppAuth
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
        override val dataPaging: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = { dao.getPagingSource() },
        remoteMediator = PostRemoteMediator(
            service = apiService,
            postDao = dao,
            postRemoteKeyDao = postRemoteKeyDao,
            db = appDb
        )
    ).flow.map { it.map(PostEntity::toPost)
        .insertSeparators { previous, _ ->
            if (previous?.id?.rem(5) == 0){
                Ad(Random.nextInt(), "figma.jpg")
            } else null
        }
    }

    override suspend fun likeById(post: Post) {
        try {
            if (post.likedByMe) {
                dao.dislikeById(post.id)
                val response = apiService.disLikeById(post.id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromPost(body))
            } else {
                dao.likeById(post.id)
                val response = apiService.likeById(post.id)
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
            val response = apiService.removeById(id)
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
            val response = apiService.save(post)
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
            val response = apiService.upload(media)
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
            val response = apiService.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { auth.setAuth(authState.id, it) }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun registration(name: String, login: String, pass: String) {
        try {
            val response = apiService.registrationUser(name, login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { auth.setAuth(authState.id, it) }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}