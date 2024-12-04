@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.core.data.profile.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostEntity
import com.mobi.ripple.core.domain.profile.model.UserProfileSimplePost
import com.mobi.ripple.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class UserSimplePostsResponse(
    val id: String,
    val image: String,
    val authorId: String,
    val creationDate: Instant
) {
    fun asUserProfileSimplePost() = UserProfileSimplePost(
        id = id,
        image = Base64.decode(image, Base64.DEFAULT),
        authorId = authorId,
        creationDate = creationDate
    )


    fun asSimplePostEntity() = SimplePostEntity(
        id = id,
        image = image,
        authorId = authorId,
        creationDate = creationDate
    )
}

