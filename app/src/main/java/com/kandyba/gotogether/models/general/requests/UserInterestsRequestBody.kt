package com.kandyba.gotogether.models.general.requests

/**
 * Модель тела запроса изменения интересов
 *
 * @constructor
 * @property interests изменяемые интересы
 */
data class UserInterestsRequestBody(
    val interests: Map<String, Int>
)