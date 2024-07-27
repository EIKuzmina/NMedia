package ru.netology.nmedia

import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemory()
    val data = repository.get()
    fun like() = repository.like()
    fun repost() = repository.repost()
}