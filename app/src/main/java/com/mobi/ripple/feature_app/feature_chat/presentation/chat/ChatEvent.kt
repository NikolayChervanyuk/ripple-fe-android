package com.mobi.ripple.feature_app.feature_chat.presentation.chat

import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageDataModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageModel

sealed class ChatEvent {
  data class SendNewMessageRequested(val messageData: MessageDataModel): ChatEvent()
  data class ResendNewMessage(val message: MessageModel) : ChatEvent() {

  }
//  data object AnotherEvent: ChatEvent()
}