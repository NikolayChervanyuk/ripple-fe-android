package com.mobi.ripple.core.presentation.followers_following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.domain.use_case.followers_following.FollowersFollowingUseCases
import com.mobi.ripple.core.presentation.followers_following.model.asFollowersFollowingSimpleUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersFollowingViewModel @Inject constructor(
    private val useCases: FollowersFollowingUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(FollowersFollowingState())
    val state: StateFlow<FollowersFollowingState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //Events triggered from Screen
    fun onEvent(event: FollowersFollowingEvent) {
        when (event) {
            is FollowersFollowingEvent.InitScreen -> {
                viewModelScope.launch {
                    launch {
                        GlobalAppManager.storedUsername?.let { storedUsername ->
                            _state.value
                                .isMeState.value = storedUsername == event.username
                        } ?: GlobalAppManager.onLogout()
                    }
                     when (event.getType) {
                        GetType.FOLLOWERS -> {
                            val response = useCases.getFollowersUseCase(
                                event.username,
                                state.value.followersPage++
                            )
                            if (!response.isError) {
                                _state.value.followersList.addAll(
                                    response.content!!.map { it.asFollowersFollowingSimpleUserModel() }
                                )
                            }

                        }

                        GetType.FOLLOWING -> {
                            val response = useCases.getFollowingUseCase(
                                event.username,
                                state.value.followingPage++
                            )
                            if (!response.isError) {
                                _state.value.followingList.addAll(
                                    response.content!!.map { it.asFollowersFollowingSimpleUserModel() }
                                )
                            }
                        }
                    }
                }
            }

            is FollowersFollowingEvent.BackButtonClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BackButtonClicked)
                }
            }

            is FollowersFollowingEvent.UserSelected -> {
                viewModelScope.launch {
                    GlobalAppManager.storedUsername?.let { username ->
                        val isMe = username == event.username
                        _eventFlow.emit(UiEvent.UserSelected(isMe, event.username))
                    } ?: GlobalAppManager.onLogout()
                }
            }

            is FollowersFollowingEvent.ExploreUsersClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ExploreUsersClicked)
                }
            }
        }
    }

    sealed class UiEvent {
        data object BackButtonClicked : UiEvent()
        data object ExploreUsersClicked : UiEvent()
        data class UserSelected(val isMe: Boolean, val selectedUsername: String) : UiEvent()
    }
}
