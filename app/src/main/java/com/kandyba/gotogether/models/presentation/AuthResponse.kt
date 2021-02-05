package com.kandyba.gotogether.models.presentation

data class AuthResponse (
    val user: String,
    val isLoyal: Boolean,
    val token: String,
    val events: Map<String, EventModel>
)