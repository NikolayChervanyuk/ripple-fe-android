package com.mobi.ripple.feature_app.feature_chat.service.scheduled

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import com.mobi.ripple.feature_app.feature_chat.service.util.NotificationHandler
import io.ktor.client.HttpClient
import javax.inject.Inject

class MessagesWorkerFactory @Inject constructor(
    private val cacheManager: MessageCacheManager,
    private val notificationHandler: NotificationHandler,
    private val client: HttpClient
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = PendingMessagesWorker(
        context = appContext,
        parameters = workerParameters,
        cacheManager = cacheManager,
        notificationHandler = notificationHandler,
        chatUseCases = cacheManager.chatUseCases,
        client = client
    )
}