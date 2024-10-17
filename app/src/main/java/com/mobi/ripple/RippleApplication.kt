package com.mobi.ripple

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.mobi.ripple.feature_app.feature_chat.service.scheduled.MessagesWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RippleApplication() : Application(), Configuration.Provider {

    @Inject
    lateinit var messagesWorkerFactory: MessagesWorkerFactory


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(messagesWorkerFactory)
            .build()
}