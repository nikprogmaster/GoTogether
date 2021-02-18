package com.kandyba.gotogether.models.data.events

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("events")
    @Expose
    val events: Map<String, EventInfoDataModel>
)

data class EventInfoDataModel(
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("photo_links")
    @Expose
    val photoLinks: List<String>,

    @SerializedName("liked_by_user")
    @Expose
    val likedByUser: Boolean,

    @SerializedName("dates")
    @Expose
    val dates: List<Date>,

    @SerializedName("price")
    @Expose
    val price: String,

    @SerializedName("is_free")
    @Expose
    val isFree: Boolean,

    @SerializedName("categories")
    @Expose
    val categories: List<String>
) {

    inner class Date (
        @SerializedName("start_unix")
        @Expose
        val startUnix: String,

        @SerializedName("end_unix")
        @Expose
        val endUnix: String
    )
}

