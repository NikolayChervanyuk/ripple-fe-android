package com.mobi.ripple.core.presentation.profile

sealed class ProfileEvent {
  data class InitializeUser(val username: String ): ProfileEvent()
  data object ChangeFollowState: ProfileEvent()
  data object FollowersClicked: ProfileEvent()
  data object FollowingClicked: ProfileEvent()
}