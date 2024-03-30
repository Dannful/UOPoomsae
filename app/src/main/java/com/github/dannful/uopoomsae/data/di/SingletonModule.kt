package com.github.dannful.uopoomsae.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.data.repository.AuthRepositoryImpl
import com.github.dannful.uopoomsae.data.repository.DispatcherProviderImpl
import com.github.dannful.uopoomsae.domain.repository.AuthRepository
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> {
        val store = preferencesDataStore(name = Constants.DATA_STORE_NAME)
        return store.getValue(application, DataStore<Preferences>::data)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        dataStore: DataStore<Preferences>,
        dispatcherProvider: DispatcherProvider
    ): AuthRepository = AuthRepositoryImpl(dataStore, dispatcherProvider)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideNetworkClient(
        authRepository: AuthRepository
    ): Flow<HttpClient> {
        val flow = authRepository.getUsername().flatMapLatest { username ->
            authRepository.getPassword().map { password ->
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
        return flow.onEach {
	        flow.firstOrNull()?.close()
        }
    }
}