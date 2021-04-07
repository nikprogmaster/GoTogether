package com.kandyba.gotogether.models.domain.auth

data class LoginDomainResponse(
    val userId: String,
    val token: String
)