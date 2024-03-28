package com.github.dannful.uopoomsae.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.data.repository.DispatcherProviderImpl
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}