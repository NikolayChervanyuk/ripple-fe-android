package com.mobi.ripple.feature_app

import android.content.Context
import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.ChatState
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.GenericMessage
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import com.mobi.ripple.isAppClosed
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.ClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

class MessageManager(
    private val context: Context,
    val cacheManager: MessageCacheManager,
    private val client: HttpClient
) {

    val cacheQueries = CacheQueries(cacheManager)

    private var isConnectionOpened = false

//    var receivedMessagesFlow: Flow<GenericMessage> = emptyFlow()
//        private set

    private val _receivedMessagesFlow = MutableSharedFlow<GenericMessage>()
    val receivedMessagesFlow: SharedFlow<GenericMessage> = _receivedMessagesFlow.asSharedFlow()


    private var webSocketSession: ClientWebSocketSession? = null

    suspend fun openConnection() {
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
//                receivedMessagesFlow = webSocketSession!!.incoming.receiveAsFlow()
//                    .map { it as Frame.Text }
//                    .map { it.readText() }
//                    .map { Json.decodeFromString<GenericMessage>(it) }
//                    .onEach { cacheManager.cache(it) }
            }
        } catch (ex: ClosedReceiveChannelException) {
            Timber.i("Receive channel closed")
        } catch (ex: Exception) {
            Timber.e("Websocket error: ${ex.message}")
        } finally {
            webSocketSession = null
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
        if (webSocketSession != null) {
            webSocketSession!!.send(Json.encodeToString(message))
            return true
        } else {
            Timber.e("Can't send message because no connection has been established")
            return false
        }
    }

    suspend fun cacheMessage(message: GenericMessage, isMine: Boolean = true, isUnread: Boolean = !isMine) {
        cacheManager.cache(message, isMine, isUnread)
    }


    class CacheQueries(private val cacheManager: MessageCacheManager) {
        suspend fun getUnreadMessagesCount(): Int =
            cacheManager.database.messageDao.getUnreadMessagesCount()

    }
}
