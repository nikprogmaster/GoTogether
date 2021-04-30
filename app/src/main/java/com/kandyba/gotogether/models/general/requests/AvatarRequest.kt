package com.kandyba.gotogether.models.general.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Кандыба Никита
 * @since 30.04.2021
 */
data class AvatarRequest(
    @SerializedName("avatar")
    @Expose
    val avatar: String
)