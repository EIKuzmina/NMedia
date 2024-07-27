package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    var likeCount: Int = 57,
    val shareByMe: Boolean,
    var share: Int = 123,
    var vieww: Int = 148
)
