package com.github.dannful.uopoomsae.domain.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.data.repository.PreferencesRepositoryImpl
import com.github.dannful.uopoomsae.data.repository.RemoteRepositoryImpl
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun providePreferencesRepository(
        dispatcherProvider: DispatcherProvider,
        dataStore: DataStore<Preferences>
    ): PreferencesRepository = PreferencesRepositoryImpl(dispatcherProvider, dataStore)

    @Provides
    @ViewModelScoped
    fun provideScoreRepository(
        clients: Flow<HttpClient>
    ): RemoteRepository = RemoteRepositoryImpl(clients)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @ViewModelScoped
    fun provideNetworkClient(
        preferencesRepository: PreferencesRepository
    ) = preferencesRepository.getUsername().flatMapLatest { username ->
        preferencesRepository.getPassword().map { password ->
            HttpClient {
                install(ContentNegotiation) {
                    json()
                }
                defaultRequest {
                    url {
                        host = Settings.SERVER_URL
                        port = Settings.SERVER_PORT
                        protocol = URLProtocol.HTTP
                    }
                }
                install(Auth) {
                    basic {
                        sendWithoutRequest { true }
                        realm = Settings.AUTH_REALM
                        credentials {
                            BasicAuthCredentials(
                                username = username,
                                password = password
                            )
                        }
                    }
                }
                install(WebSockets) {
                    contentConverter = KotlinxWebsocketSerializationConverter(Json)
                    pingInterval = 20_000
                }
                install(Resources)
            }
        }
    }
}