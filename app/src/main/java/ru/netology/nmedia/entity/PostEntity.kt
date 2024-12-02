package ru.netology.nmedia.entity

import androidx.room.*
import ru.netology.nmedia.handler.*

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val authorAvatar: String,
    val show: Boolean,
    @Embedded
    var attachment: Attachment? = null
) {
    fun toPost() = Post(
        id, author, published, content, likedByMe,
        likes, authorAvatar, show, attachment
    )

    companion object {
        fun fromPost(post: Post) =
            PostEntity(
                post.id, post.author, post.published, post.content, post.likedByMe,
                post.likes, post.authorAvatar, post.show, post.attachment
            )
    }
}

fun List<PostEntity>.toPost(): List<Post> = map(PostEntity::toPost)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromPost)