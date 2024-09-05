package com.mobi.ripple.core.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.core.domain.use_case.profile.ProfileUseCases
import com.mobi.ripple.core.presentation.profile.model.asUserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.asUserProfileSimplePostModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile.PersonalProfileViewModel.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //Events triggered from Screen
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.InitializeUser -> {
                viewModelScope.launch { event.username

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
                    if (state.value.page == 0L) {
                        launch {
                            val simplePostsResponse =
                                profileUseCases.getSimplePostsUseCase(
                                    event.username,
                                    state.value.page++
                                )
                            if (simplePostsResponse.isError) {
                                if (simplePostsResponse.httpStatusCode != HttpStatusCode.NotFound) {
                                    _eventFlow.emit(UiEvent.ShowSnackbar(simplePostsResponse.errorMessage))
                                }
                            } else {
                                _state.value.userProfileSimplePosts
                                    .addAll(simplePostsResponse.content!!
                                        .map { it.asUserProfileSimplePostModel() }
                                    )
                            }
                        }
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object BackButtonClicked : UiEvent()
    }
}
