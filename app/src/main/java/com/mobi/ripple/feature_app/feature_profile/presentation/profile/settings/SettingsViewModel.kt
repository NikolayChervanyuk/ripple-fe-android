package com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.util.invalidateBearerTokens
import com.mobi.ripple.feature_app.feature_profile.domain.use_case.settings.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases,
    private val client: HttpClient
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //Events triggered from Screen
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LogoutRequested -> {
                viewModelScope.launch { GlobalAppManager.onLogout() }
            }

            is SettingsEvent.LogoutAllDevicesRequested -> {
                viewModelScope.launch {
                    val result = settingsUseCases.logoutAllDevicesUsesCase()
                    if (result.isError) {
                        //Show error
                    } else {
                        client.invalidateBearerTokens()
                        GlobalAppManager.onLogout()
                    }
                }
            }
        }
    }

    //Events consumed in Screen, in LaunchedEffect for example
    sealed class UiEvent {
//        data object : UiEvent()
    }
}

data class SettingsState(
    val exampleState: Int = 0
)