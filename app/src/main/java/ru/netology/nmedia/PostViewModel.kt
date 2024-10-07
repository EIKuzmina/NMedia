package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFile

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryFile(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    val draft = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun clearEdit() {
        edited.value = empty
    }

    fun draftContent(content: String) {
        val text = content.trim()
        if (draft.value?.content == text) {
            return
        }
        draft.value = draft.value?.copy(content = text)
    }

    fun likeById(id: Int) = repository.likeById(id)
    fun repost(id: Int) = repository.repost(id)
    fun removeById(id: Int) = repository.removeById(id)
    fun videoById() = repository.videoById()
}