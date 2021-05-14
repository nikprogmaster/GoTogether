package com.kandyba.gotogether.models.domain.auth

/**
 * Модель ответа при авторизации (domain-слой)
 */
data class LoginDomainResponse(
    val userId: String,
    val token: String
)