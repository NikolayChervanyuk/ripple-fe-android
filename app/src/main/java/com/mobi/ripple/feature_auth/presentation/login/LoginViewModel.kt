package com.mobi.ripple.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.util.validator.FieldValidator
import com.mobi.ripple.feature_auth.domain.use_case.AuthUseCases
import com.mobi.ripple.feature_auth.presentation.register.RegisterViewModel.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.IdentifierTextChanged -> {
                _state.value.user.identifier = event.newText
            }

            is LoginEvent.PasswordTextChanged -> {
                _state.value.user.password = event.newText
            }

            is LoginEvent.Login -> {
                viewModelScope.launch {
                    if (state.value.user.identifier.isNotEmpty() &&
                        FieldValidator.isPasswordValid(state.value.user.password)
                    ) {
                        val loginResponse =
                            authUseCases.loginUseCase(_state.value.user.asUserLogin())

                        loginResponse.content?.let {
                            GlobalAppManager.saveAuthTokens(it)
                            GlobalAppManager.storedUsername?.let { username ->
                                val simpleAuthUserResponse =
                                    authUseCases.getSimpleAuthUserUseCase(username, true)
                                if (!simpleAuthUserResponse.isError) {
                                    val authUser = simpleAuthUserResponse.content!!
                                    GlobalAppManager.storeId(authUser.userId)
                                    authUser.smallPfp?.let { pfp ->
                                        GlobalAppManager.storeSmallProfilePicture(pfp)
                                    }
                                } else _eventFlow.emit(UiEvent.ShowSnackBar("Login failed, try again later"))
                            }
                            GlobalAppManager.onSuccessfulLogin()
                        } ?: _eventFlow.emit(UiEvent.ShowSnackBar("Invalid credentials"))
                    } else _eventFlow.emit(UiEvent.ShowSnackBar("Invalid credentials"))
                }
            }

            is LoginEvent.PasswordChange -> {
                TODO("Not implemented")
            }

            is LoginEvent.SignUp -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateToSignUp)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        data object NavigateToSignUp : UiEvent()
    }
}