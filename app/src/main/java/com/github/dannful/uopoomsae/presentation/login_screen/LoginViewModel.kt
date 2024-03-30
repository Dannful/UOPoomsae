package com.github.dannful.uopoomsae.presentation.login_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.repository.AuthRepository
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val remoteRepository: RemoteRepository,
    private val preferencesRepository: PreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {

        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"
        private const val LOADING_KEY = "loading"
    }

    init {
        viewModelScope.launch {
            authRepository.getUsername().collectLatest(::onUsernameChanged)
        }
        viewModelScope.launch {
            authRepository.getPassword().collectLatest(::onPasswordChanged)
        }
    }

    val username = savedStateHandle.getStateFlow(USERNAME_KEY, "")
    val password = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    val loading = savedStateHandle.getStateFlow(LOADING_KEY, false)

    fun onUsernameChanged(username: String) {
        savedStateHandle[USERNAME_KEY] = username
    }

    fun onPasswordChanged(password: String) {
        savedStateHandle[PASSWORD_KEY] = password
    }

    fun submit(onSubmit: (Boolean) -> Unit) {
        viewModelScope.launch {
            savedStateHandle[LOADING_KEY] = true
            authRepository.saveUsername(username.value)
            authRepository.savePassword(password.value)
            val userAuth = remoteRepository.getUserAuth().first()
            preferencesRepository.saveCurrentAuth(userAuth)
            savedStateHandle[LOADING_KEY] = false
            onSubmit(userAuth.level >= Permissions.USER.level)
        }
    }
}