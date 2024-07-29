package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemory : PostRepository {
    private var posts = listOf(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. " +
                    "Затем появились курсы по дизайну, разработке, аналитике и управлению. " +
                    "Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                    "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likedByMe = false,
            likeCount = 3,
            shareByMe = false,
            share = 0,
            vieww = 12
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "18 сентября в 10:12",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с " +
                    "разработкой мобильных приложений, учимся рассказывать истории и составлять " +
                    "PR-стратегию прямо на бесплатных занятиях \\uD83D\\uDC47",
            likedByMe = false,
            likeCount = 5,
            shareByMe = false,
            share = 2,
            vieww = 4
        )
    )

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
}
