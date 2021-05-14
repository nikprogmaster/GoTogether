package com.kandyba.gotogether.models.general.requests

/**
 * Модель тела запроса изменения главной информации о пользователе
 *
 * @constructor
 * @property firstName имя
 * @property birthDate дата рождения
 * @property sex пол
 */
data class UserMainRequestBody(
    val firstName: String?,
    val birthDate: Long?,
    val sex: Int?
)