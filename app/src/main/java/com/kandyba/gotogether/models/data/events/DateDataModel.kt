package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель даты события (data-слой)
 *
 * @property startUnix начало события
 * @property endUnix окончание события
 */
data class DateDataModel(
    @SerializedName("start")
    @Expose
    val startUnix: Long,

    @SerializedName("end")
    @Expose
    val endUnix: Long
)