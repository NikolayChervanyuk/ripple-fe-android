package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import androidx.paging.PagingData
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.NewPostModel
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.UserProfileSimplePostModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PersonalProfileState(
    var userProfileInfoState: MutableState<UserProfileInfoModel> = mutableStateOf(
        UserProfileInfoModel(
            id = "",
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
    var userProfilePicture: MutableState<ImageBitmap?> = mutableStateOf(null),
    var userProfileSimplePostsFlow: Flow<PagingData<UserProfileSimplePostModel>> = emptyFlow(),
    var userProfileSimplePosts: SnapshotStateList<UserProfileSimplePostModel> = mutableStateListOf(),
) {
    var editProfileState = mutableStateOf(EditProfileState())
    data class EditProfileState(
        var isUsernameTakenState: MutableState<Boolean> = mutableStateOf(false),
        var isEmailTakenState: MutableState<Boolean> = mutableStateOf(false)
    )
    var newPostState = mutableStateOf(NewPostState())
    data class NewPostState(
        var isUploading: MutableState<Boolean> = mutableStateOf(false),
        val newPostModelState: MutableState<NewPostModel> = mutableStateOf(
            NewPostModel(ByteArray(0), "")
        )
    )
}
