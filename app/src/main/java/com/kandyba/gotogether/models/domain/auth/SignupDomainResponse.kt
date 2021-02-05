package com.kandyba.gotogether.models.domain.auth

data class SignupDomainResponse (
    val id: String,
    val token: String,
    val events: Map<String, EventInfoDomainModel>
)