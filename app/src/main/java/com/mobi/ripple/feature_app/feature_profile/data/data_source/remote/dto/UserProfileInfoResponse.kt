@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class UserProfileInfoResponse(
    val id: String?,
    val fullName: String?,
    val username: String,
    val bio: String?,
    val followers: Long,
    val following: Long,
    val followed: Boolean,
    val active: Boolean,
    val lastActive: Instant,
    val postsCount: Long,
) {
    fun asUserProfileInfo() = UserProfileInfo(
        fullName = fullName,
        userName = username,
        bio = bio,
        followers = followers,
        following = following,
        isFollowed = followed,
        isActive = active,
        lastActive = lastActive,
        postsCount = postsCount,
    )
}
