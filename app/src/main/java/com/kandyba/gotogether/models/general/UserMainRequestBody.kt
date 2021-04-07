package com.kandyba.gotogether.models.general

data class UserMainRequestBody(
    val firstName: String?,
    val birthDate: Long?,
    val sex: Int?
)