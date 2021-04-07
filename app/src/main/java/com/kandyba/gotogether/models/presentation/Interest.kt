package com.kandyba.gotogether.models.presentation

data class LevelInterest(
    val name: String,
    var code: String = "",
    var level: Int = 50
)

data class SelectableInterest(
    val name: String,
    var code: String = "",
    var selected: Boolean = false
)
