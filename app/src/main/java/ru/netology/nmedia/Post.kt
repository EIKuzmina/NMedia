package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likedByMe: Boolean = false,
    var likeCount: Int = 13_278_000,
    var shareByMe: Boolean = false,
    var share: Int = 12_000
)
