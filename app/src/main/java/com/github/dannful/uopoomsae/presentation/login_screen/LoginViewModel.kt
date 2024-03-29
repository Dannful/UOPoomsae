package com.github.dannful.uopoomsae.presentation.login_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val remoteRepository: RemoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {

        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"
    }

    init {
        viewModelScope.launch {
            preferencesRepository.getUsername().collectLatest(::onUsernameChanged)
        }
        viewModelScope.launch {
            preferencesRepository.getPassword().collectLatest(::onPasswordChanged)
        }
    }

    val username = savedStateHandle.getStateFlow(USERNAME_KEY, "")
    val password = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    fun onUsernameChanged(username: String) {
        savedStateHandle[USERNAME_KEY] = username
    }

    fun onPasswordChanged(password: String) {
        savedStateHandle[PASSWORD_KEY] = password
    }

    fun submit(onSubmit: (Boolean) -> Unit) {
        viewModelScope.launch {
            preferencesRepository.saveUsername(username.value)
            preferencesRepository.savePassword(password.value)
            val userAuth = remoteRepository.getUserAuth().first()
            preferencesRepository.saveCurrentAuth(userAuth)
            onSubmit(userAuth.level >= Permissions.USER.level)
        }
    }
}