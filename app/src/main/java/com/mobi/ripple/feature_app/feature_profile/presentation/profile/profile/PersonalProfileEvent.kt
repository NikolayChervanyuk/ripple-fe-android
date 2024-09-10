package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import android.net.Uri
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel

sealed class PersonalProfileEvent {
    data class UploadPfpRequested(val imageUri: Uri) : PersonalProfileEvent()
    data object DeletePfpRequested: PersonalProfileEvent()
    data object SettingsClicked: PersonalProfileEvent()
    data object FollowersClicked: PersonalProfileEvent()
    data object FollowingClicked: PersonalProfileEvent()
    data class CreatePostRequested(val imageUri: Uri): PersonalProfileEvent()
    sealed class EditScreenEvent {
        data class UsernameTextChanged(val newText: String) : PersonalProfileEvent()
        data class EmailTextChanged(val newText: String) : PersonalProfileEvent()
        data class EditProfileInfoRequested(val userModel: UserProfileInfoModel) : PersonalProfileEvent()
    }
    sealed class NewPostScreenEvent {
        data class CaptionTextChanged(val newText: String) : PersonalProfileEvent()
        data object UploadPostRequested: PersonalProfileEvent()
    }
}