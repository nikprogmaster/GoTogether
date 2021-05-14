package com.kandyba.gotogether.models.presentation

/**
 * Модель категории (интереса) с полем level (от 0 до 100)
 */
data class LevelInterest(
    val name: String,
    var code: String = "",
    var level: Int = 50
)

/**
 * Модель категории (интереса), которую можно выбрать
 */
data class SelectableInterest(
    val name: String,
    var code: String = "",
    var selected: Boolean = false
)
