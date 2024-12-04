package com.mobi.ripple.feature_app.feature_chat.domain.use_case

data class ChatUseCases(
    val getChatsFlowUseCase: GetChatsFlowUseCase,
    val findChatUsersLikeUseCase: FindChatUsersLikeUseCase,
    val createNewChatUseCase: CreateNewChatUseCase,
    val getChatUserUseCase: GetChatUserUseCase,
    val hasPendingMessagesUseCase: HasPendingMessagesUseCase,
    val getChatParticipantsUseCase: GetChatParticipantsUseCase,
    val getChatUseCase: GetChatUseCase,
    val getMessagesFlowUseCase: GetMessagesFlowUseCase
)