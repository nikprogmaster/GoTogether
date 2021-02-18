package com.kandyba.gotogether.models.domain.auth

import com.kandyba.gotogether.models.domain.events.EventInfoDomainModel

data class SignupDomainResponse(
    val id: String,
    val token: String,
    val events: Map<String, EventInfoDomainModel>
)