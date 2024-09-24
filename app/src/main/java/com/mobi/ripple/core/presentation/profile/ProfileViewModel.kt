package com.mobi.ripple.core.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.mobi.ripple.core.domain.profile.use_case.ProfileUseCases
import com.mobi.ripple.core.presentation.profile.model.asUserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.asUserProfileSimplePostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.InitializeUser -> {
                viewModelScope.launch {
                    launch {
                        val profileInfoResponse =
                            profileUseCases.getProfileInfoUseCase(event.username)
                        if (profileInfoResponse.isError) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    profileInfoResponse.errorMessage
                                )
                            )
                        } else {
                            _state.value.userProfileInfoState.value =
                                profileInfoResponse.content!!.asUserProfileInfoModel()
                        }
                    }
                    launch {
                        val profilePictureResponse =
                            profileUseCases.getProfilePictureUseCase(event.username)
                        if (profilePictureResponse.isError) {
                            if (profilePictureResponse.httpStatusCode != HttpStatusCode.NotFound) {
                                _eventFlow.emit(UiEvent.ShowSnackbar(profilePictureResponse.errorMessage))
                            }
                        } else {
                            _state.value.userProfilePicture.value =
                                profilePictureResponse.content!!.image
                        }
                    }
                    launch {
                        _state.value.userProfileSimplePostsFlow =
                            profileUseCases
                                .getSimplePostsFlowUseCase(event.username)
                                .map { pagingData ->
                                    pagingData.map { it.asUserProfileSimplePostModel() }
                                }
                    }
                }
            }

            is ProfileEvent.ChangeFollowState -> {
                viewModelScope.launch {
                    val response = profileUseCases
                        .changeFollowingStateUseCase(
                            state.value.userProfileInfoState.value
                                .userName
                        )
                    if (response.isError) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Service unavailable, try again later"))
                        return@launch
                    }
                    var followers = _state.value.userProfileInfoState.value
                        .followers
                    var isFollowed = _state.value.userProfileInfoState.value
                        .isFollowed
                    followers += if (isFollowed) -1 else 1
                    isFollowed = !isFollowed

                    _state.value.userProfileInfoState.value =
                        _state.value.userProfileInfoState.value
                            .copy(followers = followers, isFollowed = isFollowed)
                }
            }

            is ProfileEvent.FollowersClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.FollowersClicked)
                }
            }

            is ProfileEvent.FollowingClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.FollowingClicked)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object BackButtonClicked : UiEvent()
        data object FollowersClicked : UiEvent()
        data object FollowingClicked : UiEvent()
    }
}
