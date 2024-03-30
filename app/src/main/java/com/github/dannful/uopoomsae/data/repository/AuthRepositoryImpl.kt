package com.github.dannful.uopoomsae.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.domain.repository.AuthRepository
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider
) : AuthRepository {
    override fun getUsername() =
        dataStore.data.mapNotNull { it[stringPreferencesKey(Constants.USERNAME_KEY)] }
            .distinctUntilChanged()

    override suspend fun saveUsername(username: String) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[stringPreferencesKey(Constants.USERNAME_KEY)] = username
            }
        }
    }

    override fun getPassword() =
        dataStore.data.mapNotNull { it[stringPreferencesKey(Constants.PASSWORD_KEY)] }
            .distinctUntilChanged()

    override suspend fun savePassword(password: String) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[stringPreferencesKey(Constants.PASSWORD_KEY)] = password
            }
        }
    }
}