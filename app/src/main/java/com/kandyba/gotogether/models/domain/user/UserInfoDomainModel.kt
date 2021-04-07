package com.kandyba.gotogether.models.domain.user

data class UserInfoDomainModel(
    var tokens: List<String>,
    var id: String,
    var passwordDigest: String,
    var email: String,
    var firstName: String? = null,
    var phone: String? = null,
    var birthDate: String? = null,
    var sex: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var createdAt: String,
    var updatedAt: String,
    var info: String? = null,
    var isLoyal: Boolean
)