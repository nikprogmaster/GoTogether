package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DateDataModel(
    @SerializedName("start_unix")
    @Expose
    val startUnix: String,

    @SerializedName("end_unix")
    @Expose
    val endUnix: String
)