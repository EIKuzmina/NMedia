package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likeCount: Int = 0,
    val shareByMe: Boolean,
    val share: Int = 0,
    val viewByMe: Boolean,
    val view: Int = 0,
    val video:String
) {
    fun toPost() = Post(id, author, content, published, likedByMe, likeCount,
        shareByMe, share, viewByMe, view, video)

    companion object {
        fun fromPost(post: Post) =
            PostEntity(post.id, post.author, post.content, post.published, post.likedByMe, post.likeCount,
                post.shareByMe, post.share, post.viewByMe, post.view, post.video)
    }
}
