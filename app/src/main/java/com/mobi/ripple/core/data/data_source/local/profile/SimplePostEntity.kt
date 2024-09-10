package com.mobi.ripple.core.data.data_source.local.profile

import android.util.Base64
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost

@Entity
data class SimplePostEntity (
    @PrimaryKey
    val id: String,
    val image : String,
    val authorId: String
) {
    fun asUserProfileSimplePost() = UserProfileSimplePost(
        id = id,
        image = Base64.decode(image, Base64.DEFAULT),
        authorId = authorId
    )
}
