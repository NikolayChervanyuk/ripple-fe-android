package com.mobi.ripple.feature_app.feature_search.domain.model

data class SearchSimpleUser(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val profilePicture: ByteArray?
)
