package com.kandyba.gotogether.models.data.messages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kandyba.gotogether.models.data.user.UserInfoDataModel

data class DialogDataModel(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("companion")
    @Expose
    val companion: UserInfoDataModel,

    @SerializedName("withBlockedUser")
    @Expose
    val withBlockedUser: Boolean,

    @SerializedName("blockedByCompanion")
    @Expose
    val blockedByCompanion: Boolean,

    @SerializedName("lastMessage")
    @Expose
    val lastMessage: MessageDataModel? = null
)