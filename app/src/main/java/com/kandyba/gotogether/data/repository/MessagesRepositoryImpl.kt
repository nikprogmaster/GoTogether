package com.kandyba.gotogether.data.repository

import android.util.Log
import com.kandyba.gotogether.data.api.MessagesApiMapper
import com.kandyba.gotogether.data.converter.messages.DialogDataConverter
import com.kandyba.gotogether.data.converter.messages.MessageDataConverter
import com.kandyba.gotogether.models.DialogResponse
import com.kandyba.gotogether.models.Socket
import com.kandyba.gotogether.models.domain.messages.DialogDomainModel
import com.kandyba.gotogether.models.domain.messages.MessageDomainModel
import com.kandyba.gotogether.models.general.SocketMessage
import io.reactivex.Single
import org.json.JSONException
import java.util.concurrent.TimeUnit


class MessagesRepositoryImpl(
    private val apiMapper: MessagesApiMapper,
    private val messageConverter: MessageDataConverter,
    private val dialogConverter: DialogDataConverter
) : MessagesRepository {

    private var socket: Socket? = null

    override fun startMessaging(token: String, messageListener: Socket.UniversalListener) {
        start(token, messageListener)
    }

    override fun sendMessage(token: String, socketMessage: SocketMessage): Boolean {
        try {
            Log.v(SOCKET_TAG, "Try to send data")
            return socket?.send(socketMessage) ?: false
        } catch (e: JSONException) {
            Log.e(SOCKET_TAG, "Try to send data with wrong JSON format, data: $socketMessage")
        }
        return false
    }

    override fun getUserDialogs(token: String): Single<List<DialogDomainModel>> {
        return apiMapper.getUserDialogs(token).map { dialogConverter.convert(it) }
    }

    override fun getDialogMessages(
        token: String,
        dialogId: String
    ): Single<List<MessageDomainModel>> {
        return apiMapper.getDialogMessages(token, dialogId).map { messageConverter.convert(it) }
    }

    override fun createDialog(token: String, companionId: String): Single<DialogResponse> {
        return apiMapper.createDialog(token, companionId)
    }

    override fun getState(): Socket.State {
        return socket?.state ?: Socket.State.CLOSED
    }

    private fun start(token: String, messageListener: Socket.UniversalListener) {
        if (socket == null) {
            socket = Socket.Builder
                .with(URL)
                .addHeader(AUTHORIZATION_KEY, token)
                .setPingInterval(5, TimeUnit.SECONDS)
                .build()
            socket?.setUniversalListener(messageListener)
            socket?.connect()
        }
    }

    companion object {
        private const val URL = "ws://51.15.104.77:8085/api/ws"
        private const val AUTHORIZATION_KEY = "ssid"
        private const val SOCKET_TAG = "MessageSocket"
    }
}