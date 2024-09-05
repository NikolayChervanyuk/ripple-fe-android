package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.NewPostModel
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.UserProfileSimplePostModel

data class PersonalProfileState(
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
    var userProfileSimplePosts: SnapshotStateList<UserProfileSimplePostModel> = mutableStateListOf(),
    var page: Long = 0L
) {
    var editProfileState = mutableStateOf(EditProfileState())
    data class EditProfileState(
        var isUsernameTakenState: MutableState<Boolean> = mutableStateOf(false),
        var isEmailTakenState: MutableState<Boolean> = mutableStateOf(false)
    )
    var newPostState = mutableStateOf(NewPostState())
    data class NewPostState(
        val newPostModelState: MutableState<NewPostModel> = mutableStateOf(
            NewPostModel(ByteArray(0), "")
        )
    )
}
