package com.mobi.ripple.feature_app.feature_profile.presentation.profile.profile

import android.net.Uri
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.UserProfileInfoModel

sealed class ProfileEvent {
    data class UploadPfpRequested(val imageUri: Uri) : ProfileEvent()
    data object DeletePfpRequested: ProfileEvent()
    data object SettingsClicked: ProfileEvent()
    data class UploadPostRequested(val imageUri: Uri): ProfileEvent()
    sealed class EditScreenEvent {
        data class UsernameTextChanged(val newText: String) : ProfileEvent()
        data class EmailTextChanged(val newText: String) : ProfileEvent()
        data class EditProfileInfoRequested(val userModel: UserProfileInfoModel) : ProfileEvent()
    }
}