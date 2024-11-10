package ru.netology.nmedia.handler


data class Post(
    val id: Int = 0,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val viewByMe: Boolean = false,
    val view: Int = 0,
    val video: String= "",
    val authorAvatar: String
)
