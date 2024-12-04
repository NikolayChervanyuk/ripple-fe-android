package com.mobi.ripple.core.presentation.followers_following

sealed class FollowersFollowingEvent {
  data class InitScreen(val username: String, val getType: GetType): FollowersFollowingEvent()
  data object BackButtonClicked: FollowersFollowingEvent()
  data object ExploreUsersClicked: FollowersFollowingEvent()
  data class UserSelected(val username: String): FollowersFollowingEvent()
}