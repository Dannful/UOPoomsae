package com.github.dannful.uopoomsae.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.dannful.uopoomsae.BuildConfig
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.data.repository.AuthRepositoryImpl
import com.github.dannful.uopoomsae.data.repository.DispatcherProviderImpl
import com.github.dannful.uopoomsae.domain.repository.AuthRepository
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
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

    @Provides
    @Singleton
    fun provideNetworkClient(): HttpClient =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(HttpCookies)
            defaultRequest {
                host = BuildConfig.SERVER_HOST
                port = BuildConfig.SERVER_PORT
            }
            install(Resources)
        }
}