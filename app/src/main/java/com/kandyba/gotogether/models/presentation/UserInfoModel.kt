package com.kandyba.gotogether.models.presentation


class UserInfoModel(
    var id: String,
    var email: String,
    var firstName: String? = null,
    var phone: String? = null,
    var birthDate: String? = null,
    var sex: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var info: String? = null,
    var isLoyal: Boolean,
    val avatar: String? = null,
    val interests: Map<String, Int>
)
