package com.mobi.ripple.feature_app.feature_chat.presentation.new_chat

import androidx.compose.ui.util.fastMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.ChatUseCases
import com.mobi.ripple.feature_app.feature_chat.presentation.model.SimpleChatUserModel
import com.mobi.ripple.feature_app.feature_chat.presentation.model.asSimpleChatUserModel
import com.mobi.ripple.feature_app.feature_chat.presentation.model.asSimpleUserItemModel
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.toChatCreatedMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NewChatViewModel.NewChatViewModelFactory::class)
class NewChatViewModel @AssistedInject constructor(
    @Assisted val messageManager: MessageManager,
    private val chatUseCases: ChatUseCases
) : ViewModel() {
    private var searchTextChangedJob: Job? = null

    private val _state = MutableStateFlow(NewChatState())
    val state: StateFlow<NewChatState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //Events triggered from Screen
    fun onEvent(event: NewChatEvent) {
        when (event) {
            is NewChatEvent.SearchTextChanged -> {
                searchTextChangedJob = viewModelScope.launch {
                    searchTextChangedJob?.let {
                        if (it.isActive) it.cancel()
                    }
                    delay(200)
                    val response = chatUseCases.findChatUsersLikeUseCase(event.text)
                    if (!response.isError) {
                        _state.value.foundUsersList.value = response.content!!
                            .filter { it.id != GlobalAppManager.storedId }
                            .filter { foundUser ->
                                state.value.addedParticipantsList
                                    .find { foundUser.id == it.userId } == null
                            }
                            .fastMap { it.asSimpleUserItemModel() }
                    }
                }
            }

            is NewChatEvent.ChatNameTextChanged -> {
                _state.value.chatName.value = event.text
            }

            is NewChatEvent.CreateNewChat -> {
                viewModelScope.launch {
                    if (!FieldValidator.isChatNameValid(state.value.chatName.value)) return@launch
                    val response = chatUseCases.createNewChatUseCase(
                        state.value.chatName.value.ifBlank { null },
                        event.participants.map { it.userId }
                    )
                    if (!response.isError) {
                        messageManager.cacheMessage(response.content!!.toChatCreatedMessage())
                        _eventFlow.emit(
                            UiEvent.ChatCreatedSuccessfully(response.content.chatId)
                        )
                    }
                }
            }
        }
    }

    suspend fun getInitialUser(userId: String): SimpleChatUserModel? {
        val response = chatUseCases.getChatUserUseCase(userId)
        return response.content?.asSimpleChatUserModel()
    }

    //Events consumed in Screen, in LaunchedEffect for example
    sealed class UiEvent {
        data class ChatCreatedSuccessfully(val chatId: String) : UiEvent()
    }

    @AssistedFactory
    interface NewChatViewModelFactory {
        fun create(messageManager: MessageManager): NewChatViewModel
    }
}
