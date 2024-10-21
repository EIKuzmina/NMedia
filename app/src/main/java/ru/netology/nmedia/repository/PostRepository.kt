package ru.netology.nmedia.repository

import ru.netology.nmedia.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Int)
    fun repost(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
    fun videoById()
    fun dislikeById(id: Int)
}