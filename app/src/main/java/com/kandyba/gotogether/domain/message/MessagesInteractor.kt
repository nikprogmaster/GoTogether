package com.kandyba.gotogether.domain.message

import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import com.kandyba.gotogether.models.presentation.Message
import io.reactivex.Single

/**
 * Интерактор для сообщений и диалогов
 */
interface MessagesInteractor {

    /**
     * Начать переписку (открыть сокет)
     *
     * @param token токен сессии пользователя
     * @param socketListener слушатель событий сокета
     */
    fun startMessaging(token: String, socketListener: Socket.UniversalListener)

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
     * @param userId id пользователя
     * @return [Single] список сообщений
     */
    fun getDialogMessages(token: String, dialogId: String, userId: String): Single<List<Message>>

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