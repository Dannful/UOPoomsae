package com.github.dannful.uopoomsae.domain.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        client: HttpClient
    ): RemoteRepository = RemoteRepositoryImpl(client)
}