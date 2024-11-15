package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.handler.Post

class PostRepositoryImpl : PostRepository {
    override fun getAllAsync(callback: PostRepository.PostRepositoryCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                } else {
                    callback.onError(
                        RuntimeException
                            ("error code: ${response.code()} with ${response.message()}")
                    )
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun likeByIdAsync(
        id: Int,
        likedByMe: Boolean,
        callback: PostRepository.PostRepositoryCallback<Post>
    ) {
        if (likedByMe) {
            PostsApi.retrofitService.disLikeById(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        callback.onError(RuntimeException
                            ("error code: ${response.code()} with ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
        } else {
            PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        callback.onError(RuntimeException("error code: ${response.code()} with ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }
            })
        }
    }

    override fun saveAsync(
        post: Post,
        callback: PostRepository.PostRepositoryCallback<Post>
    ) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()} with ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }

    override fun repostAsync(
        id: Int,
        callback: PostRepository.PostRepositoryCallback<Post>
    ) {
    }

    override fun removeByIdAsync(
        id: Int,
        callback: PostRepository.PostRepositoryCallback<Unit>
    ) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(Unit)
                } else {
                    callback.onError(RuntimeException("error code: ${response.code()} with ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception(t))
            }
        })
    }
}