package com.kandyba.gotogether.domain.message

import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import com.kandyba.gotogether.models.presentation.Message
import io.reactivex.Single

/**
 * @author Кандыба Никита
 * @since 25.04.2021
 */
interface MessagesInteractor {

    fun startMessaging(token: String, socketListener: Socket.UniversalListener)

    fun sendMessage(token: String, socketMessage: SocketMessage): Boolean

    fun getUserDialogs(token: String): Single<List<DialogDomainModel>>

    fun getDialogMessages(token: String, dialogId: String, userId: String): Single<List<Message>>

    fun createDialog(token: String, companionId: String): Single<DialogResponse>

    fun getState(): Socket.State
}