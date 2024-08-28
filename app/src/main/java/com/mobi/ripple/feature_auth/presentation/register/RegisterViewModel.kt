package com.mobi.ripple.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.util.validator.AuthFieldValidator
import com.mobi.ripple.feature_auth.domain.use_case.AuthUseCases
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
class RegisterViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var lastTimeUsernameChanged: Instant = Instant.MIN
    private var lastTimeEmailChanged: Instant = Instant.MIN

    private var isEmailTakenJob: Job? = null
    private var isUsernameTakenJob: Job? = null

    //Events triggered from Screen
    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.BackButtonPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.RouteBack)
                }
            }

            is RegisterEvent.FullNameChanged -> {
                _state.value.userRegister.fullName = event.newText
                _state.value.showFullNameInvalidError.value =
                    !AuthFieldValidator.isFullNameValid(event.newText)
            }

            is RegisterEvent.UsernameChanged -> {
                _state.value.userRegister.username = event.newText
                _state.value.isUsernameInvalid.value =
                    !AuthFieldValidator.isUsernameValid(event.newText)

                state.value.showUsernameTaken.value = !state.value.isUsernameInvalid.value

                if (!state.value.isUsernameInvalid.value) {
                    isUsernameTakenJob = viewModelScope.launch {
                        if (!isDelayPassed(lastTimeUsernameChanged)) {
                            isUsernameTakenJob?.cancel()
                        }
                        delay(300L)
                        val response = authUseCases.isUsernameTakenUseCase(event.newText)
                        if (!response.isError) {
                            state.value.isUsernameTaken.value = response.content!!
                        }
                    }
                }
                lastTimeUsernameChanged = Instant.now()
            }

            is RegisterEvent.EmailChanged -> {
                _state.value.userRegister.email = event.newText
                _state.value.showEmailInvalidError.value =
                    !AuthFieldValidator.isEmailValid(event.newText)
                if (!state.value.showEmailInvalidError.value) {
                    isEmailTakenJob = viewModelScope.launch {
                        if (!isDelayPassed(lastTimeEmailChanged)) {
                            isEmailTakenJob?.cancel()
                        }
                        delay(300L)
                        val response = authUseCases.isEmailTakenUseCase(event.newText)
                        if (!response.isError) {
                            _state.value.isEmailTaken.value = response.content!!
                        }
                    }
                }
                lastTimeUsernameChanged = Instant.now()
            }

            is RegisterEvent.PasswordChanged -> {
                _state.value.userRegister.password = event.newText
                _state.value.showPasswordsNotMatchingError.value =
                    event.newText != state.value.userRegister.password

                _state.value.isPasswordInvalid.value =
                    !AuthFieldValidator.isPasswordValid(state.value.userRegister.password)
            }

            is RegisterEvent.ConfirmPasswordChanged -> {
                _state.value.userRegister.confirmPassword = event.newText
                _state.value.hasConfirmPasswordBeenChanged.value = true

                _state.value.showPasswordsNotMatchingError.value =
                    (state.value.hasConfirmPasswordBeenChanged.value) &&
                            (event.newText != state.value.userRegister.password)

                _state.value.hasConfirmPasswordBeenChanged =
                    state.value.showPasswordsNotMatchingError
            }

            is RegisterEvent.RegisterButtonClicked -> {
                if (state.value.showFullNameInvalidError.value ||
                    state.value.isUsernameInvalid.value ||
                    state.value.showEmailInvalidError.value ||
                    state.value.isPasswordInvalid.value ||
                    state.value.showPasswordsNotMatchingError.value
                ) return //TODO: show which field is wrong
                else {
                    viewModelScope.launch {
                        val registerResponse = authUseCases
                            .registerUseCase(_state.value.userRegister.asUserRegister())

                        if (registerResponse.isError) {
                            _state.value.registrationErrorMessage.value =
                                registerResponse.errorMessage
                            _state.value.showRegistrationError.value = true
                        } else {
                            val loginResponse = authUseCases
                                .loginUseCase(state.value.userRegister.asUserLogin())

                            if (loginResponse.isError && loginResponse.content == null) {
                                _eventFlow.emit(UiEvent.ShowSnackBar(loginResponse.errorMessage))
                            } else {
                                GlobalAppManager.onSuccessfulLogin(loginResponse.content!!)
                            }
                        }
                    }
                }
            }
        }
    }

    //Events consumed in Screen, in LaunchedEffect for example
    sealed class UiEvent {
        data object RouteBack : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
//        data object RouteToMainScreen: UiEvent()
    }

    private fun isDelayPassed(lastInvoked: Instant, delayMilli: Long = 300L): Boolean {
        return Instant.now().isAfter(lastInvoked.plusMillis(delayMilli))
    }
}

