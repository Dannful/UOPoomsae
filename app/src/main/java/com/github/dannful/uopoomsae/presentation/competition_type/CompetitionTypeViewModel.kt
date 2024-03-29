package com.github.dannful.uopoomsae.presentation.competition_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionTypeViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    fun onUserAuth(onAuth: (Boolean) -> Unit) {
        viewModelScope.launch {
            val userAuth = preferencesRepository.getCurrentAuth().first()
            onAuth(userAuth.level >= Permissions.USER.level)
        }
    }
}