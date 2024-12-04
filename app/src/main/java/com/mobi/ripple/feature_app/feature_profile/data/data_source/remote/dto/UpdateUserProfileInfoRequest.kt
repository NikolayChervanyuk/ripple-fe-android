package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto

import com.mobi.ripple.core.domain.profile.model.UserProfileInfo
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileInfoRequest(
    val fullName: String,
    val username: String,
    val email: String,
    val bio: String
)

fun UserProfileInfo.asUpdateUserProfileInfoRequest() =
    UpdateUserProfileInfoRequest(
        fullName = fullName!!,
        username = userName,
        email = email!!,
        bio = bio!!
    )
