package com.mobi.ripple.feature_app.feature_profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.ProfileUseCases
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.asUserProfileInfoModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.asUserProfileSimplePostModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        viewModelScope.launch {
            GlobalAppManager.storedUsername?.let { username ->
                launch {
                    val profileInfoResponse = profileUseCases.getProfileInfoUseCase(username)
                    if (profileInfoResponse.isError) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(profileInfoResponse.errorMessage))
                    } else {
                        _state.value.userProfileInfo =
                            profileInfoResponse.content!!.asUserProfileInfoModel()
                        launch {
                            val profilePictureResponse =
                                profileUseCases.getProfilePictureUseCase(username)
                            if (profilePictureResponse.isError) {
                                _eventFlow.emit(UiEvent.ShowSnackbar(profileInfoResponse.errorMessage))
                            } else {
                                _state.value.userProfilePicture =
                                    profilePictureResponse.content!!.image
                            }
                        }
                        launch {
                            val simplePostsResponse =
                                profileUseCases.getSimplePostsUseCase(username, state.value.page++)

                            if (simplePostsResponse.isError) {
                                _eventFlow.emit(UiEvent.ShowSnackbar(simplePostsResponse.errorMessage))
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
    //Events triggered from Screen
    fun onEvent(event: ProfileEvent) {
//        when (event) {
//            is ProfileEvent. -> {
//            }
//        }
    }

    //Events consumed in Screen, in LaunchedEffect for example
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
