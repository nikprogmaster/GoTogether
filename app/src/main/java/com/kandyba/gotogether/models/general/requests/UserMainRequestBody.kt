package com.kandyba.gotogether.models.general.requests

data class UserMainRequestBody(
    val firstName: String?,
    val birthDate: Long?,
    val sex: Int?
)