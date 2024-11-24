package com.mobi.ripple.feature_app

import android.content.Context
import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.MessageEntity
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.GenericMessage
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.ClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

class MessageManager(
    private val context: Context,
    val cacheManager: MessageCacheManager,
    private val client: HttpClient
) {
    private var isConnectionOpened = false

    private val _receivedMessagesFlow = MutableSharedFlow<GenericMessage>()
    val receivedMessagesFlow: SharedFlow<GenericMessage> = _receivedMessagesFlow.asSharedFlow()

    private var webSocketSession: ClientWebSocketSession? = null

    suspend fun openConnection() {
        if (!isConnectionOpened) {
            try {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = AppUrls.HOST,
                    path = AppUrls.WEBSOCKET_MESSAGES_PATH,
                    port = AppUrls.PORT
                ) {
                    isConnectionOpened = true
                    webSocketSession = this
                    while (true) {
                        val message = receiveDeserialized<GenericMessage>()
                        cacheManager.cache(message)
                        _receivedMessagesFlow.emit(message)
                    }
                }
            } catch (ex: ClosedReceiveChannelException) {
                Timber.i("Receive channel closed")
            } catch (ex: Exception) {
                Timber.e("Websocket error: ${ex.message}")
            } finally {
                isConnectionOpened = false
                webSocketSession = null
            }
        }
    }

    suspend fun closeConnection(reason: String = "Client closed") {
        webSocketSession?.close(
            CloseReason(
                CloseReason.Codes.GOING_AWAY,
                reason
            )
        ) ?: Timber.w("Close connection requested, but there is no connection established")
        Timber.i("Connection closed")
        isConnectionOpened = false
    }

    suspend fun sendMessage(message: GenericMessage): Boolean {
        try {
            if (webSocketSession != null) {
                webSocketSession!!.send(Json.encodeToString(message))
                return true
            } else {
                Timber.e("Can't send message because no connection has been established")
                return false
            }
        } catch (ex: Exception) {
            return false
        }
    }

    suspend fun cacheMessage(
        message: GenericMessage,
        isMine: Boolean = true,
        isUnread: Boolean = !isMine,
        isSent: Boolean = false
    ): MessageEntity? {
        return cacheManager.cache(message, isMine, isUnread, isSent)
    }
}
