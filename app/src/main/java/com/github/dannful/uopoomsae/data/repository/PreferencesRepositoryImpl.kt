package com.github.dannful.uopoomsae.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    override suspend fun saveTableId(tableId: Int) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[intPreferencesKey(Constants.TABLE_ID_KEY)] = tableId
            }
        }
    }

    override fun getTableId() =
        dataStore.data.mapNotNull { it[intPreferencesKey(Constants.TABLE_ID_KEY)] }.distinctUntilChanged()

    override suspend fun setCompetitionMode(competition: Boolean) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[booleanPreferencesKey(Constants.COMPETITION_MODE_LABEL)] = competition
            }
        }
    }

    override fun getCompetitionMode() =
        dataStore.data.map { it[booleanPreferencesKey(Constants.COMPETITION_MODE_LABEL)] ?: true }.distinctUntilChanged()

    override suspend fun saveJudgeId(judgeId: Int) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[intPreferencesKey(Constants.JUDGE_ID_KEY)] = judgeId
            }
        }
    }

    override fun getJudgeId() =
        dataStore.data.mapNotNull { it[intPreferencesKey(Constants.JUDGE_ID_KEY)] }.distinctUntilChanged()

    override fun getUsername() =
        dataStore.data.mapNotNull { it[stringPreferencesKey(Constants.USERNAME_KEY)] }.distinctUntilChanged()

    override suspend fun saveUsername(username: String) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[stringPreferencesKey(Constants.USERNAME_KEY)] = username
            }
        }
    }

    override fun getPassword() =
        dataStore.data.mapNotNull { it[stringPreferencesKey(Constants.PASSWORD_KEY)] }.distinctUntilChanged()

    override suspend fun savePassword(password: String) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[stringPreferencesKey(Constants.PASSWORD_KEY)] = password
            }
        }
    }

    override fun getCurrentAuth() = dataStore.data.mapNotNull { preferences ->
        preferences[intPreferencesKey(Constants.AUTH_KEY)]?.let {
            Permissions.entries[it]
        }
    }.distinctUntilChanged()

    override suspend fun saveCurrentAuth(permissions: Permissions) {
        withContext(dispatcherProvider.IO) {
            dataStore.edit {
                it[intPreferencesKey(Constants.AUTH_KEY)] = permissions.ordinal
            }
        }
    }
}