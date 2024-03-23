package com.github.dannful.uopoomsae.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.data.repository.DispatcherProviderImpl
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
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun provideNetworkClient() = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        install(Auth) {
            basic {
                realm = Settings.SOCKET_AUTH_REALM
                credentials {
                    BasicAuthCredentials(
                        username = Settings.SOCKET_AUTH_USERNAME,
                        password = Settings.SOCKET_AUTH_PASSWORD
                    )
                }
            }
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
            pingInterval = 20_000
        }
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> {
        val store = preferencesDataStore(name = Constants.DATA_STORE_NAME)
        return store.getValue(application, DataStore<Preferences>::data)
    }
}