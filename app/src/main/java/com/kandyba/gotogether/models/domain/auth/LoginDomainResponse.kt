package com.kandyba.gotogether.models.domain.auth

import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel

data class LoginDomainResponse(
    val user: String,
    val isLoyal: Boolean,
    val token: String,
    val events: Map<String, EventInfoDomainModel>
)