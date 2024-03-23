package com.github.dannful.uopoomsae.data.repository

import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class DispatcherProviderImpl : DispatcherProvider {

    override val IO: CoroutineContext
        get() = Dispatchers.IO

    override val Main: CoroutineContext
        get() = Dispatchers.Main

    override val Default: CoroutineContext
        get() = Dispatchers.Default

    override val Unconfined: CoroutineContext
        get() = Dispatchers.Unconfined
}