package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Кандыба Никита
 * @since 06.02.2021
 */
data class UserRequestBody(
    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("birthDate")
    @Expose
    val birthDate: Long? = null,

    @SerializedName("sex")
    @Expose
    val sex: Int? = null,

    @SerializedName("phone")
    @Expose
    val phone: String? = null
)