package ru.netology.nmedia.model

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import ru.netology.nmedia.auth.*
import ru.netology.nmedia.repository.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val repository: PostRepository
) : ViewModel() {
    val data: LiveData<AuthState> = appAuth.authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() =  appAuth.authStateFlow.value.id != 0
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