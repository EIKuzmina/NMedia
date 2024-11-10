package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val viewByMe: Boolean,
    val view: Int = 0,
    val video:String,
    val authorAvatar: String
) {
    fun toPost() = Post(id, author, content, published, likedByMe, likes,
        viewByMe, view, video, authorAvatar)

    companion object {
        fun fromPost(post: Post) =
            PostEntity(post.id, post.author, post.content, post.published, post.likedByMe, post.likes,
                 post.viewByMe, post.view, post.video, post.authorAvatar)
    }
}
