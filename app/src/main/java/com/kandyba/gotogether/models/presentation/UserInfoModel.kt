package com.kandyba.gotogether.models.presentation

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
class UserInfoModel(
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
