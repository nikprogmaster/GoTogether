package com.kandyba.gotogether.models.general.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель тела запроса для регистрации пользователя
 *
 * @constructor
 * @property email почта пользователя
 * @property password пароль
 * @property firstName имя пользователя
 * @property sex пол пользователя
 * @property birthdate дата рождения
 * @property info информация из секции
 */
data class SignupRequestBody(
    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("password")
    @Expose
    val password: String,

    @SerializedName("firstName")
    @Expose
    val firstName: String = "",

    @SerializedName("sex")
    @Expose
    val sex: Int = 0,

    @SerializedName("birthdate")
    @Expose
    val birthdate: Int = 0,

    @SerializedName("info")
    @Expose
    val info: String = ""
)