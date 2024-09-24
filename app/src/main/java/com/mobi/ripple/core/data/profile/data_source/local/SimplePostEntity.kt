package com.mobi.ripple.core.data.profile.data_source.local

import android.util.Base64
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobi.ripple.core.domain.profile.model.UserProfileSimplePost
import java.time.Instant

@Entity
data class SimplePostEntity (
    @PrimaryKey
    val id: String,
    val image : String,
    val authorId: String,
    val creationDate: Instant
) {
    fun asUserProfileSimplePost() = UserProfileSimplePost(
        id = id,
        image = Base64.decode(image, Base64.DEFAULT),
        authorId = authorId,
        creationDate = creationDate
    )
}
