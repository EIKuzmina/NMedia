package ru.netology.nmedia.repository

import ru.netology.nmedia.handler.Post

interface PostRepository {
    fun getAllAsync(callback: PostRepositoryCallback<List<Post>>)
    fun likeByIdAsync(id: Int, likedByMe: Boolean, callback: PostRepositoryCallback<Post>)
    fun repostAsync(id: Int, callback: PostRepositoryCallback<Post>)
    fun removeByIdAsync(id: Int, callback: PostRepositoryCallback<Post>)
    fun saveAsync(post: Post, callback: PostRepositoryCallback<Post>)

    interface PostRepositoryCallback<T> {
        fun onSuccess(result: T)
        fun onError(e: Exception)
    }
}