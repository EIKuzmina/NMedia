package ru.netology.nmedia.auth

import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.handler.PushToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "id"
    private val tokenKey = "token"
    private val _authStateFlow: MutableStateFlow<AuthState>
    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)
        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id.toInt(), token))
        }
        sendPushToken()
    }
    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()
    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface AppAuthEntryPoint {
        fun getApiService(): ApiService
    }
    private val entryPoint =
        EntryPointAccessors.fromApplication(context, AppAuthEntryPoint::class.java)
    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = AuthState(id.toInt(), token)
        with(prefs.edit()) {
            putLong(idKey, id.toLong())
            putString(tokenKey, token)
            apply()
        }
        sendPushToken()
    }
    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            commit()
        }
        sendPushToken()
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                entryPoint.getApiService().sendPushToken(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @Volatile
    private var instance: AppAuth? = null

    fun getInstance(): AppAuth = synchronized(this) {
        instance ?: throw IllegalStateException(
            "AppAuth is not initialized, you must call AppAuth.initializeApp(Context context) first."
        )
    }
}
data class AuthState(val id: Int = 0, val token: String? = null)