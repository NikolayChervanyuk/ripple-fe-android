package com.mobi.ripple.feature_app.feature_chat.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.filter
import androidx.paging.map
import com.mobi.ripple.feature_app.MessageManager
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.ChatUseCases
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.model.asSimpleChatModel
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ChatsViewModel.ChatsViewModelFactory::class)
class ChatsViewModel @AssistedInject constructor(
    @Assisted val messageManager: MessageManager,
    private val chatUseCases: ChatUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ChatsState())
    val state: StateFlow<ChatsState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchTextChangedJob: Job? = null

    init {
        viewModelScope.launch {
            _state.value.chatsFlow = chatUseCases.getChatsFlowUseCase(messageManager.cacheManager).map { pagingData ->
                pagingData.map { it.asSimpleChatModel() }
            }
        }
    }

    fun onEvent(event: ChatsEvent) {
        when (event) {
            is ChatsEvent.GetChatsFlow -> {
                //TODO
            }

            is ChatsEvent.SearchChatsTextChanged -> {
                if(event.text.isBlank()) return
                searchTextChangedJob = viewModelScope.launch {
                    searchTextChangedJob?.cancel()
                    delay(250)
                    _state.value.chatsFlow = _state.value.chatsFlow.map { pagingData ->
                        pagingData.filter {
                            it.chatName.contains(
                                other = event.text,
                                ignoreCase = true
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class OpenChat(val chatId: String) : UiEvent()
    }

    @AssistedFactory
    interface ChatsViewModelFactory {
        fun create(messageManager: MessageManager): ChatsViewModel
    }
}
