package ru.netology.nmedia.handler

data class Post(
    val id: Int = 0,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val authorAvatar: String
)
