package com.mobi.ripple.feature_app.feature_search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.util.DEFAULT_DELAY_MILLI
import com.mobi.ripple.core.util.isDelayPassed
import com.mobi.ripple.feature_app.feature_search.domain.use_case.SearchUseCases
import com.mobi.ripple.feature_app.feature_search.presentation.model.asSimpleUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCases: SearchUseCases
) : ViewModel() {

    private var findUsersLikeJob: Job? = null
    private var lastTimeSearchTextChanged: Instant = Instant.MIN

    private val _state = MutableStateFlow<SearchState>(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //Events triggered from Screen
    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchTextChanged -> {
                if (event.newText.isBlank()) return

                findUsersLikeJob = viewModelScope.launch {
                    if (!isDelayPassed(lastTimeSearchTextChanged)) {
                        findUsersLikeJob?.cancel()
                    }
                    delay(DEFAULT_DELAY_MILLI)
                    val result = searchUseCases.findUsersLikeUsernameUseCase(event.newText)
                    if (result.isError) {
                        _eventFlow.emit(UiEvent.SearchError)
                        return@launch
                    }
                    _state.value.foundUsersList.clear()
                    _state.value.foundUsersList.addAll(result.content!!
                        .map { it.asSimpleUserModel() })
                }
                lastTimeSearchTextChanged = Instant.now()
            }
            is SearchEvent.UserItemClicked -> {
                viewModelScope.launch {
                    GlobalAppManager.storedUsername?.let { storedUsername ->
                        val isMe = storedUsername == event.username
                            _eventFlow.emit(UiEvent.UserItemClicked(isMe, event.username))
                    } ?: GlobalAppManager.onLogout()
                }
            }
        }
    }

    sealed class UiEvent {
        data object SearchError : UiEvent()
        data class UserItemClicked(val isMe: Boolean,val username: String) : UiEvent()
    }
}
