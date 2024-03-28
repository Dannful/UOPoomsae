package com.github.dannful.uopoomsae.presentation.login_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {

        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"
    }

    init {
        preferencesRepository.getUsername().onEach(::onUsernameChanged).launchIn(viewModelScope)
        preferencesRepository.getPassword().onEach(::onPasswordChanged).launchIn(viewModelScope)
    }

    val username = savedStateHandle.getStateFlow(USERNAME_KEY, "")
    val password = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    fun onUsernameChanged(username: String) {
        savedStateHandle[USERNAME_KEY] = username
    }

    fun onPasswordChanged(password: String) {
        savedStateHandle[PASSWORD_KEY] = password
    }

    fun submit() {
        viewModelScope.launch {
            preferencesRepository.saveUsername(username.value)
            preferencesRepository.savePassword(password.value)
        }
    }
}