package ru.netology.nmedia.entity

import androidx.room.*
import ru.netology.nmedia.handler.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val authorAvatar: String
) {
    fun toPost() = Post(id, author, published, content, likedByMe, likes, authorAvatar)

    companion object {
        fun fromPost(post: Post) =
            PostEntity(
                post.id, post.author, post.published, post.content,
                post.likedByMe, post.likes, post.authorAvatar
            )
    }
}

    fun List<PostEntity>.toPost(): List<Post> = map(PostEntity::toPost)
    fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromPost)