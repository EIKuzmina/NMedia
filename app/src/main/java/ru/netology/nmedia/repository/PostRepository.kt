package ru.netology.nmedia.repository

import ru.netology.nmedia.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Int, likedByMe: Boolean): Post
    fun repost(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
}