package com.kandyba.gotogether.models.presentation

data class Interest(
    val name: String,
    var code: String = "",
    var level: Int = 50
)