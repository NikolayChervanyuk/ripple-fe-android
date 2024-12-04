package com.mobi.ripple.feature_app.feature_chat.presentation.chats

sealed class ChatsEvent {
//  data class SomtheingChanged(val newText: String): ChatsEvent()
  data object GetChatsFlow: ChatsEvent()
  data class SearchChatsTextChanged(val text: String): ChatsEvent()
}