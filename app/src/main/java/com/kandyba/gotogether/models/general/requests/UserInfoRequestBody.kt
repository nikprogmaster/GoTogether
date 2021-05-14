package com.kandyba.gotogether.models.general.requests

/**
 * Модель тела запроса для изменения информации блока "обо мне"
 *
 * @constructor
 * @property info новая информация
 */
data class UserInfoRequestBody(
    val info: String
)