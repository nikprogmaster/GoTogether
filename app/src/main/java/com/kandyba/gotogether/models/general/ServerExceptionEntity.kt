package com.kandyba.gotogether.models.general

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServerExceptionEntity(
    @SerializedName("errors")
    @Expose
    var errors: List<Error>? = null
)

data class Error(
    @SerializedName("detail")
    @Expose
    var detail: String? = null
)