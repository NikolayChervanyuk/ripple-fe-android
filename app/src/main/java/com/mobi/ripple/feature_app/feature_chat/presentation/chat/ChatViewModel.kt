package com.mobi.ripple.feature_app.feature_chat.presentation.chat

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.ChatUseCases
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.MessageModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chat.model.toChatModel
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.model.asSimpleChatModel
import com.mobi.ripple.feature_app.feature_chat.presentation.model.asSimpleChatUserModel
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatCreatedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatOpenedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewMessageContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.NewMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Instant

@HiltViewModel(assistedFactory = ChatViewModel.ChatViewModelFactory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted private val chatId: String,
    @Assisted val messageManager: MessageManager,
    private val chatUseCases: ChatUseCases
) : ViewModel() {
    private val defaultLoadErrorMessage = "Loading failed"

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val activeMessageIdResends = mutableSetOf<Long>()

    init {
        viewModelScope.launch {
            val chatModel = chatUseCases.getChatUseCase(chatId).asSimpleChatModel().toChatModel()

            chatModel.chatParticipants =
                chatUseCases.getChatParticipantsUseCase(chatId)?.let { list ->
                    list.map { it.asSimpleChatUserModel() }.associateBy { it.userId }
                } ?: emptyMap()

            if (chatModel.chatParticipants.isEmpty()) {
                _eventFlow.emit(UiEvent.LoadError(defaultLoadErrorMessage))
            }
            _state.value.chatModel.value = chatModel

            _state.value.messagesFlow = chatUseCases.getMessagesFlowUseCase(chatId, messageManager.cacheManager)
                .map { pagingData ->
                    pagingData.map { message ->
                        var authorUsername: String? = null
                        var authorPfp: ImageBitmap? = null
                        var isAuthorActive = false

                        when (message.eventType) {
                            ChatEventType.NEW_MESSAGE -> {
                                val content =
                                    Json.decodeFromString<NewMessageContent>(message.messageDataJson)
                                authorUsername =
                                    chatModel.chatParticipants[content.senderId]?.username
                                authorPfp = chatModel.chatParticipants[content.senderId]?.userPfp
                                isAuthorActive = chatModel.chatParticipants[content.senderId]?.isActive ?: false
                            }

                            ChatEventType.CHAT_OPENED -> {
                                val content =
                                    Json.decodeFromString<ChatOpenedContent>(message.messageDataJson)
                                authorUsername =
                                    chatModel.chatParticipants[content.userId]?.username
                                authorPfp = chatModel.chatParticipants[content.userId]?.userPfp
                            }

                            ChatEventType.CHAT_CREATED -> {
                                val content =
                                    Json.decodeFromString<ChatCreatedContent>(message.messageDataJson)
                                authorUsername =
                                    chatModel.chatParticipants[content.creatorId]?.username
                                authorPfp = chatModel.chatParticipants[content.creatorId]?.userPfp
                            }

                            else -> {}
                        }
                        if (!message.isMine && authorUsername == null) {
                            throw IllegalStateException(
                                "Message sent is not owned, yet no username is provided. " +
                                        "This should be a logic error"
                            )
                        }
                        MessageModel(
                            messageId = message.messageId,
                            eventType = message.eventType,
                            chatId = message.chatId,
                            sentDate = message.sentDate,
                            defaultTextMessage = message.defaultTextMessage,
                            isMine = message.isMine,
                            isUnread = message.isUnread,
                            isSent = message.isSent,
                            messageDataJson = message.messageDataJson,
                            authorUsername = authorUsername,
                            authorPfp = authorPfp,
                            isAuthorActive = isAuthorActive
                        )
                    }
                }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendNewMessageRequested -> {
                viewModelScope.launch {
                    val newMessage = NewMessage(
                        eventType = ChatEventType.NEW_MESSAGE,
                        sentDate = Instant.now(),
                        messageData = NewMessageContent(
                            senderId = "",
                            chatId = chatId,
                            message = event.messageData.text,
                            fileName = event.messageData.fileName,
                            fileExtension = event.messageData.fileExtension
                        )
                    )
//                    val cachedMessage = messageManager.cacheMessage(newMessage)
//                    _eventFlow.emit(UiEvent.MessageCached)
                    if(messageManager.sendMessage(newMessage)){
//                        cachedMessage?.let {
//                            messageManager.cacheManager.database.messageDao
//                                .updateMessage(it.copy(isSent = true))
//                        }
                        _eventFlow.emit(UiEvent.MessageSent)
                    } else {
                        messageManager.cacheMessage(newMessage)
                        _eventFlow.emit(UiEvent.MessageCached)
                    }
                }
            }
            is ChatEvent.ResendNewMessage -> {
                viewModelScope.launch {
                    if(activeMessageIdResends.contains(event.message.messageId)) {
                        return@launch
                    }
                    activeMessageIdResends.add(event.message.messageId)
                    if(messageManager.sendMessage(event.message.toNewMessage())) {
                        val cachedMessage = messageManager.cacheManager.database.messageDao
                            .getMessageById(event.message.messageId)
                        messageManager.cacheManager.database.messageDao
                            .updateMessage(cachedMessage.copy(isSent = true, sentDate = Instant.now()))
                        _eventFlow.emit(UiEvent.MessageSent)
                    }
                    activeMessageIdResends.remove(event.message.messageId)
                }
            }
        }
    }

    fun markChatMessagesAsRead() {
        viewModelScope.launch {
            messageManager.cacheManager.database.messageDao.markAsRead(chatId)
        }
    }

    sealed class UiEvent {
        data object MessageCached : UiEvent()
        data object MessageSent : UiEvent()
        data class LoadError(val errorMessage: String) : UiEvent()
    }

    @AssistedFactory
    interface ChatViewModelFactory {
        fun create(chatId: String, messageManager: MessageManager): ChatViewModel
    }
}

