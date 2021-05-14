package com.kandyba.gotogether.models.data.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель ответа при регистрации пользователя (data-слой)
 *
 * @property id id пользователя
 */
data class SignupDataResponse(
    @SerializedName("id")
    @Expose
    var id: String
)


