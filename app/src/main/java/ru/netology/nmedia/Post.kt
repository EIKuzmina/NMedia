package ru.netology.nmedia

data class Post(
    val id: Int = 0,
    val author: String = "",
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shareByMe: Boolean = false,
    val share: Int = 0,
    val viewByMe: Boolean = false,
    val view: Int = 0,
    val video: String= ""
)