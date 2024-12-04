package ru.netology.nmedia.handler

data class Post(
    val id: Int = 0,
    val author: String = "",
    val authorId: Int,
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val authorAvatar: String,
    val show: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false
)

data class User(
    val id: Int,
    val author: String,
    val login: String,
    val password: String
)

data class Attachment(
    val url: String,
    val type: AttachmentType
)
enum class AttachmentType {
    IMAGE
}
