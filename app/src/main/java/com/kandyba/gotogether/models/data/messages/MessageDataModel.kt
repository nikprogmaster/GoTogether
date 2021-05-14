package com.kandyba.gotogether.models.data.messages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Модель сообщения (data-слой)
 */
class MessageDataModel(
    @SerializedName("userId")
    @Expose
    val userId: String,

    @SerializedName("dialogID")
    @Expose
    val dialogId: String,

    @SerializedName("text")
    @Expose
    val text: String,

    @SerializedName("createdAt")
    @Expose
    val createdAt: Long
)