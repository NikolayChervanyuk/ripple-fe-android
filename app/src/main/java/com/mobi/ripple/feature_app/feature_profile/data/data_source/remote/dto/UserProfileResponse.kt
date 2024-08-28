package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserProfileResponse(

    @SerialName("full_name")
    val fullName: String?,
    @SerialName("user_name")
    val userName: String,
    val email: String?,
    val bio: String?,
    val followers: Long,
    val following: Long,
    @SerialName("posts_count")
    val postsCount: Long,
    @SerialName("profile_image")
    val userImage: ByteArray?,
    @SerialName("simple_posts")
    val simpleUserPosts: List<ByteArray> = emptyList()
)