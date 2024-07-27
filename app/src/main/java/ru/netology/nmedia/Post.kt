package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likeCount: Int,
    val shareByMe: Boolean,
    val share: Int,
    val vieww: Int
)
