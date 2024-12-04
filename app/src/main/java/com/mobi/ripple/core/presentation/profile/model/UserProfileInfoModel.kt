package com.mobi.ripple.core.presentation.profile.model

import com.mobi.ripple.core.domain.profile.model.UserProfileInfo
import java.time.Instant

data class UserProfileInfoModel(
    val id: String,
    var fullName: String?,
    var userName: String,
    var email: String?,
    var bio: String?,
    var followers: Long,
    val following: Long,
    var isFollowed: Boolean,
    val isActive: Boolean,
    val lastActive: Instant?,
    var postsCount: Long
) {
    fun asUserProfileInfo() = UserProfileInfo(
        id = id,
        fullName = fullName ?: "",
        userName = userName,
        email = email ?: "",
        bio = bio ?: "",
        followers = followers,
        following = following,
        isFollowed = isFollowed,
        isActive = isActive,
        lastActive = lastActive ?: Instant.now(),
        postsCount = postsCount
    )

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is UserProfileInfoModel -> id == other.id &&
                    fullName == other.fullName &&
                    userName == other.userName &&
                    email == other.email &&
                    bio == other.bio

            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

fun UserProfileInfo.asUserProfileInfoModel() = UserProfileInfoModel(
    id = id,
    fullName = fullName,
    userName = userName,
    email = email,
    bio = bio,
    followers = followers,
    following = following,
    isFollowed = isFollowed,
    isActive = isActive,
    lastActive = lastActive,
    postsCount = postsCount
)
