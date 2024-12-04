package com.mobi.ripple.feature_app.feature_profile.presentation.profile.settings

sealed class SettingsEvent {
//  data class SomtheingChanged(val newText: String): SettingsEvent()
  data object LogoutRequested: SettingsEvent()
  data object LogoutAllDevicesRequested: SettingsEvent()
}