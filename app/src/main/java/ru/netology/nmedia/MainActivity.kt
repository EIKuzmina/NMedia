package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. " +
                    "Затем появились курсы по дизайну, разработке, аналитике и управлению. " +
                    "Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                    "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            base.text = post.content
            like.text = formatNumber(post.likeCount)
            repost.text = formatNumber(post.share)

            if (post.likedByMe) {
                likes?.setImageResource(R.drawable.ic_liked_24)
            }

            likes?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24
                    else R.drawable.baseline_favorite_border_24
                )
                if (post.likedByMe) post.likeCount++ else post.likeCount--
                like?.text = formatNumber(post.likeCount)
            }

            reposts?.setOnClickListener {
                post.shareByMe = !post.shareByMe
                post.share ++
                repost?.text = formatNumber(post.share)
            }
        }
    }
}