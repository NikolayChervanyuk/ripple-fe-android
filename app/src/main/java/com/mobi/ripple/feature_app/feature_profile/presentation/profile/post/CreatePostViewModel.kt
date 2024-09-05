package com.mobi.ripple.feature_app.feature_profile.presentation.profile.post

import androidx.lifecycle.ViewModel
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile.PersonalProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val personalProfileUseCases: PersonalProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow<CreatePostState>(CreatePostState())
    val state: StateFlow<CreatePostState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //Events triggered from Screen
    fun onEvent(event: CreatePostEvent) {
//        when (event) {
//            is CreatePostEvent. -> {
//            }
//        }
    }

    //Events consumed in Screen, in LaunchedEffect for example
    sealed class UiEvent {
//        data object : UiEvent()
    }
}



data class CreatePostState(
    val exampleState: Int = 0
)