package ru.netology.nmedia

import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemory()
    val data = repository.getAll()
    fun likeById(id: Int) = repository.likeById(id)
    fun repost(id: Int) = repository.repost(id)
}