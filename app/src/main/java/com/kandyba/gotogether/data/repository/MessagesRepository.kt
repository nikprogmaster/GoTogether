package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import io.reactivex.Single

/**
 * @author Кандыба Никита
 * @since 24.04.2021
 */
interface MessagesRepository {

    fun startMessaging(token: String, messageListener: Socket.UniversalListener)

    fun sendMessage(token: String, socketMessage: SocketMessage): Boolean

    fun getUserDialogs(token: String): Single<List<DialogDomainModel>>

    fun getDialogMessages(token: String, dialogId: String): Single<List<MessageDomainModel>>

    fun createDialog(token: String, companionId: String): Single<DialogResponse>

    fun getState(): Socket.State
}
