package com.github.dannful.uopoomsae.domain.repository

interface DispatcherProvider {

    val IO: kotlin.coroutines.CoroutineContext
    val Main: kotlin.coroutines.CoroutineContext
    val Default: kotlin.coroutines.CoroutineContext
    val Unconfined: kotlin.coroutines.CoroutineContext
}