package com.kandyba.gotogether.domain.message

import com.kandyba.gotogether.data.repository.MessagesRepository
import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import com.kandyba.gotogether.models.presentation.Message
import io.reactivex.Single

/**
 * Реализация интерактора для сообщений и диалогов
 *
 * @property messagesRepository репозиторий сообщений
 * @property messagesConverter конвертер сообщений
 */
class MessagesInteractorImpl(
    private val messagesRepository: MessagesRepository,
    private val messagesConverter: MessageDomainConverter
) : MessagesInteractor {

    override fun startMessaging(token: String, socketListener: Socket.UniversalListener) {
        messagesRepository.startMessaging(token, socketListener)
    }

    override fun sendMessage(token: String, socketMessage: SocketMessage): Boolean {
        return messagesRepository.sendMessage(token, socketMessage)
    }

    override fun getUserDialogs(token: String): Single<List<DialogDomainModel>> {
        return messagesRepository.getUserDialogs(token)
    }

    override fun getDialogMessages(
        token: String,
        dialogId: String,
        userId: String
    ): Single<List<Message>> {
        return messagesRepository.getDialogMessages(token, dialogId)
            .map { messagesConverter.convert(it, userId) }
    }

    override fun createDialog(token: String, companionId: String): Single<DialogResponse> {
        return messagesRepository.createDialog(token, companionId)
    }

    override fun getState(): Socket.State {
        return messagesRepository.getState()
    }
}