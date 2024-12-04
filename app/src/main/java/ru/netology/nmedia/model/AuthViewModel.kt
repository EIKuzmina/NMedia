package ru.netology.nmedia.model

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.netology.nmedia.auth.*
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.*

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao())
    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id.toInt() != 0

    fun authentication(login: String, pass: String) {
        viewModelScope.launch {
            repository.authentication(login, pass)
        }
    }
    fun registration(name: String, login: String, pass: String) {
        viewModelScope.launch {
            repository.registration(name, login, pass)
        }
    }
}