package com.github.dannful.uopoomsae.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val username: String,
    val password: String
)
