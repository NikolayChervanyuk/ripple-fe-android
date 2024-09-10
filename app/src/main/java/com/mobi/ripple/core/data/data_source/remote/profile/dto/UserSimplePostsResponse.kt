package com.mobi.ripple.core.data.data_source.remote.profile.dto

import android.util.Base64
import com.mobi.ripple.core.data.data_source.local.profile.SimplePostEntity
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import kotlinx.serialization.Serializable

@Serializable
data class UserSimplePostsResponse(
    val id: String,
    val image: String,
    val authorId: String
) {
    fun asUserProfileSimplePost() = UserProfileSimplePost(
        id = id,
        image = Base64.decode(image, Base64.DEFAULT),
        authorId = authorId
    )


    fun asSimplePostEntity() = SimplePostEntity(
        id = id,
        image = image,
        authorId = authorId
    )
}

