package com.mobi.ripple.core.presentation.profile.model

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import java.time.Instant

data class UserProfileInfoModel(
    var fullName: String?,
    var userName: String,
    var email: String?,
    var bio: String?,
    val followers: Long,
    val following: Long,
    val isFollowed: Boolean,
    val isActive: Boolean,
    val lastActive: Instant?,
    var postsCount: Long
) {
    fun asUserProfileInfo() = UserProfileInfo(
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
        return when(other){
            is UserProfileInfoModel ->
                fullName.equals(other.fullName) &&
                        userName == other.userName &&
                        email.equals(other.email) &&
                        bio.equals(other.bio) &&
                        followers == other.followers &&
                        following == other.following &&
                        isFollowed == other.isFollowed &&
                        isActive == other.isActive &&
                        postsCount == other.postsCount
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = fullName?.hashCode() ?: 0
        result = 31 * result + userName.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (bio?.hashCode() ?: 0)
        result = 31 * result + followers.hashCode()
        result = 31 * result + following.hashCode()
        result = 31 * result + isFollowed.hashCode()
        result = 31 * result + isActive.hashCode()
        result = 31 * result + postsCount.hashCode()
        return result
    }
}

fun UserProfileInfo.asUserProfileInfoModel() = UserProfileInfoModel(
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
