package ru.netology.nmedia.repository

import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll() = dao.getAll().map { list ->
        list.map {
            it.toPost()
        }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromPost(post))
    }

    override fun repost(id: Int) {
        dao.repost(id)
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun videoById() {

    }
}