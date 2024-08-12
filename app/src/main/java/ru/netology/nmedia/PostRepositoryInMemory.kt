package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemory : PostRepository {
    private var nextId = 1
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Если что-то называют чем-то чего-то, значит это вообще ничто",
            likedByMe = false,
            likeCount = 3,
            shareByMe = false,
            share = 0,
            view = 12
        ),
        Post(
            id = nextId++,
            author = "Pikabu, Русскоязычное информационно-развлекательное сообщество",
            published = "18 сентября в 10:12",
            content = "Многозадачность — опасный миф, в который многие продолжают верить. " +
                    "Переключение между задачами требует времени и мысленных усилий, " +
                    "от этого страдает и скорость, и качество работы. " +
                    "В обучении и в жизни лучше работает однозадачность.",
            likedByMe = false,
            likeCount = 5,
            shareByMe = false,
            share = 2,
            view = 4
        ),
        Post(
            id = nextId++,
            author = "Pikabu, Русскоязычное информационно-развлекательное сообщество",
            published = "12 августа в 12:16",
            content = "D'oh!",
            likeCount = 3,
            share = 0,
            view = 12,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likeCount = if (it.likedByMe) it.likeCount - 1 else
                    it.likeCount + 1
            )
        }
        data.value = posts
    }

    override fun repost(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                share = it.share + 1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun videoById() {
        data.value = posts
    }
}