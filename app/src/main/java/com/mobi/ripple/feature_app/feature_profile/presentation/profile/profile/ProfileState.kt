package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.UserProfileSimplePostModel

data class ProfileState(
    var userProfileInfoState: MutableState<UserProfileInfoModel> = mutableStateOf(
        UserProfileInfoModel(
            fullName = "",
            userName = "",
            email = "",
            bio = "",
            followers = 0L,
            following = 0L,
            isFollowed = false,
            isActive = false,
            lastActive = null,
            postsCount = 0L
        )
    ),
    var userProfilePicture: MutableState<ByteArray?> = mutableStateOf(null),
    val userProfileSimplePosts: MutableList<UserProfileSimplePostModel> = mutableListOf(),
    var page: Long = 0L,
    var newPostImageBytes: ByteArray = ByteArray(0)
) {
    var editProfileState = mutableStateOf(EditProfileState())
    data class EditProfileState(
        var isUsernameTakenState: MutableState<Boolean> = mutableStateOf(false),
        var isEmailTakenState: MutableState<Boolean> = mutableStateOf(false)
    )
}
