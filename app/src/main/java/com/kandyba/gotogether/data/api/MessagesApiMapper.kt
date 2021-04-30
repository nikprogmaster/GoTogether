package com.kandyba.gotogether.data.api

import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.data.messages.DialogDataModel
import com.kandyba.gotogether.models.data.messages.MessageDataModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface MessagesApiMapper {

    @GET(USER_DIALOGS_ENDPOINT)
    fun getUserDialogs(@Header(AUTHORIZATION_VALUE) token: String): Single<List<DialogDataModel>>

    @GET("$USER_DIALOGS_ENDPOINT/{$DIALOG_VALUE}")
    fun getDialogMessages(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(DIALOG_VALUE) dialogId: String
    ): Single<List<MessageDataModel>>

    @POST("$USER_DIALOGS_ENDPOINT/{$COMPANION_VALUE}")
    fun createDialog(
        @Header(AUTHORIZATION_VALUE) token: String,
        @Path(COMPANION_VALUE) companionId: String
    ): Single<DialogResponse>

    companion object {
        private const val USER_DIALOGS_ENDPOINT = "dialogs"
        private const val AUTHORIZATION_VALUE = "ssid"
        private const val DIALOG_VALUE = "dialogId"
        private const val COMPANION_VALUE = "companionId"
    }
}