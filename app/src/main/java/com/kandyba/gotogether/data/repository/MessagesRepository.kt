package com.kandyba.gotogether.data.repository

import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import io.reactivex.Single

/**
 * Репозиторий сообщений
 */
interface MessagesRepository {

    /**
     * Начать переписку (открыть сокет)
     *
     * @param token токен сессии пользователя
     * @param messageListener слушатель событий сокета
     */
    fun startMessaging(token: String, messageListener: Socket.UniversalListener)

    /**
     * Отправить сообщение с помощью сокета
     *
     * @param token токен сессии пользователя
     * @param socketMessage сообщение с дополнительной информацией о нем
     * @return [Boolean] отправлено ли сообщение
     */
    fun sendMessage(token: String, socketMessage: SocketMessage): Boolean

    /**
     * Получить диалоги пользователя
     *
     * @param token токен сессии пользователя
     * @return [Single] список диалогов
     */
    fun getUserDialogs(token: String): Single<List<DialogDomainModel>>

    /**
     * Получить сообщения диалога
     *
     * @param token токен сессии пользователя
     * @param dialogId id диалога
     * @return [Single] список сообщений
     */
    fun getDialogMessages(token: String, dialogId: String): Single<List<MessageDomainModel>>

    /**
     * Создать диалог
     *
     * @param token токен сессии пользователя
     * @param companionId id компаньона
     * @return [Single] c id диалога
     */
    fun createDialog(token: String, companionId: String): Single<DialogResponse>

    /**
     * Получить состояния сокета
     *
     * @return [Socket.State] состояние сокета
     */
    fun getState(): Socket.State
}
