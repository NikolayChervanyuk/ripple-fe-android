package com.mobi.ripple.feature_app.feature_chat.service.scheduled

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.ChatUseCases
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.GenericMessage
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageResolver
import com.mobi.ripple.feature_app.feature_chat.service.util.NotificationHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltWorker
class PendingMessagesWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted parameters: WorkerParameters,
    private val cacheManager: MessageCacheManager,
    private val notificationHandler: NotificationHandler,
    private val chatUseCases: ChatUseCases,
    private val client: HttpClient
) : CoroutineWorker(context, parameters) {

    companion object {
        const val TAG_NAME = "PENDING_MESSAGES"
    }

    @RequiresPermission(POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        if (isStopped) return Result.success()

        val pendingMessagesResponse = chatUseCases.hasPendingMessagesUseCase()

        if (pendingMessagesResponse.isError) return Result.failure()

        var workerResult = Result.success()
        if (!pendingMessagesResponse.content!!) return workerResult

        client.webSocket(
            method = HttpMethod.Get,
            host = AppUrls.HOST,
            path = AppUrls.WEBSOCKET_MESSAGES_PATH,
            port = AppUrls.PORT
        ) {
            var syncEnded = false

            var nextMessageAwaitTimeJob: Job = launch { delay(10_000) }
            try {
                while (!syncEnded) {
                    val message = receiveDeserialized<GenericMessage>()
                    Timber.i("Received new message of type ${message.eventType.literal}")
                    if (nextMessageAwaitTimeJob.isActive) {
                        nextMessageAwaitTimeJob.cancel()
                    }

                    launch {
                        cacheManager.cache(message)
                        Timber.i("Cache ${message.eventType.literal} successful")

                        notificationHandler.createNewChatMessageNotification(
                            MessageResolver.getChatId(message)
                        )
                    }

                    nextMessageAwaitTimeJob = launch {
                        delay(5_000)
                        syncEnded = true
                        close(
                            CloseReason(
                                CloseReason.Codes.NORMAL,
                                "No new messages sent for 5 seconds"
                            )
                        )
                        Timber.i("No new messages sent for 5 seconds, connection closed")
                    }
                }
            } catch (ex: ClosedReceiveChannelException) {
                Timber.w("Websocket connection closed by server: ${ex.message}")
                workerResult = Result.failure()
            } catch (ex: Exception) {
                Timber.e("Worker exception: ${ex.message}")
                workerResult = Result.failure()
            }
        }
        return workerResult
    }

}