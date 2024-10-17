package com.mobi.ripple.feature_app.feature_chat.presentation.chat

import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageDataModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageModel

sealed class ChatEvent {
  data class SendMessageRequested(val messageData: MessageDataModel): ChatEvent()
//  data object AnotherEvent: ChatEvent()
}