package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.*
import com.google.firebase.messaging.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.handler.PushMessage
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    @Inject
    lateinit var appAuth: AppAuth
    private val channelId = "channel"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val pushMessage = gson.fromJson(message.data["content"], PushMessage::class.java)
        val appAuthToken = appAuth.authStateFlow.value.id.toInt()
        when (pushMessage.recipientId) {
            appAuthToken, null
                -> notification(pushMessage.content)
            else -> appAuth.sendPushToken()
        }
        message.data["action"]?.let { action ->
            try {
                if (Actions.entries.map { it.name }.contains(action))
                    when (Actions.valueOf(action)) {
                        Actions.LIKE -> handleLike(
                            gson.fromJson(
                                message.data["content"],
                                Like::class.java
                            )
                        )

                        Actions.NEW_POST -> newPost(
                            gson.fromJson(
                                message.data["content"],
                                NewPost::class.java
                            )
                        )
                    }
            } catch (e: RuntimeException) {
                println("Error!")
            }
        }
    }

    private fun notification(content: String) {
        val intent = Intent(this, AppActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentText(
                content
            )
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()
        checkPermission()
        NotificationManagerCompat.from(this).notify(
            Random.nextInt(100_000),
            notification
        )
    }

    private fun handleLike(like: Like) {
        val resultIntent = Intent(this, AppActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    like.userName,
                    like.postAuthor,
                )
            )
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        checkPermission()
        NotificationManagerCompat.from(this).notify(Random.nextInt(1_000), notification)
    }

    private fun newPost(post: NewPost) {
        val resultIntent = Intent(this, AppActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    post.userName
                )
            )
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(post.content))
            .build()
        checkPermission()
        NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
    }

    override fun onNewToken(token: String) {
        appAuth.sendPushToken(token)
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }
}

enum class Actions {
    LIKE, NEW_POST
}

data class Like(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val postAuthor: String,
)

data class NewPost(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val content: String
)