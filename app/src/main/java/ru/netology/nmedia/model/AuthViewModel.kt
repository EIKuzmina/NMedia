package ru.netology.nmedia.model

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import ru.netology.nmedia.auth.*
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val repository: PostRepository
) : ViewModel() {
    val data = appAuth.authStateFlow
    val authenticated: Boolean
        get() = data.value.id.toInt() != 0
    private val _authState = MutableLiveData<Result<Unit>>()
    val authState: LiveData<Result<Unit>>
        get() = _authState

    fun authentication(login: String, pass: String) {
        viewModelScope.launch {
            try {
                repository.authentication(login, pass)
                _authState.value = Result.success(Unit)
            } catch (e: Exception) {
                _authState.value = Result.failure(Exception("Registration failed"))
            }
        }
    }

    fun registration(name: String, login: String, pass: String) {
        viewModelScope.launch {
            repository.registration(name, login, pass)
        }
    }
}