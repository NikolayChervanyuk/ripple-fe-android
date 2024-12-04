package com.mobi.ripple.feature_app.feature_chat.presentation.new_chat

import com.mobi.ripple.feature_app.feature_chat.presentation.model.SimpleChatUserModel

sealed class NewChatEvent {
    data class SearchTextChanged(val text: String): NewChatEvent()
    data class CreateNewChat(val participants: List<SimpleChatUserModel>): NewChatEvent()
    data class ChatNameTextChanged(val text: String): NewChatEvent()
    //data object AnotherEvent : NewChatEvent()
}