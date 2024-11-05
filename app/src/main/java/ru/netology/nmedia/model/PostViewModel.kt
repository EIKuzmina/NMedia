package ru.netology.nmedia.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    val draft = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.PostRepositoryCallback<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                _data.postValue(FeedModel(posts = result, empty = result.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.PostRepositoryCallback<Post> {
                override fun onSuccess(post: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    edited.value
                }
            })
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

    fun likeById(id: Int, likedByMe: Boolean) {
        repository.likeByIdAsync(id, likedByMe, object : PostRepository.PostRepositoryCallback<Post> {
            override fun onSuccess(result: Post) {
                val refreshOld = _data.value?.posts?.map {
                    if (it.id == id) {
                        result
                    } else {
                        it
                    }
                }.orEmpty()
                val resultList = if (_data.value?.posts == refreshOld) {
                    listOf(result) + refreshOld
                } else {
                    refreshOld
                }
                _data.postValue(
                    _data.value?.copy(posts = resultList)
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun removeById(id: Int) {
        val old = _data.value?.posts.orEmpty()
        repository.removeByIdAsync(id, object : PostRepository.PostRepositoryCallback<Post> {
            override fun onSuccess(result: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id })
                )
            }
                            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
    }

    fun draftContent(content: String) {
        val text = content.trim()
        if (draft.value?.content == text) {
            return
        }
        draft.value = draft.value?.copy(content = text)
    }

    fun clearEdit() {
        edited.value = empty
    }

    fun repost(id: Int) =
        repository.repostAsync(id, object : PostRepository.PostRepositoryCallback<Post> {
            override fun onSuccess(result: Post) {
            }
            override fun onError(e: Exception) {
            }
        })
}