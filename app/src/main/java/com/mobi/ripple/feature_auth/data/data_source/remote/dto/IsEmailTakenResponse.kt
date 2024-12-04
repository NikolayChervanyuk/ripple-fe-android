package com.mobi.ripple.feature_auth.data.data_source.remote.dto

data class IsEmailTakenResponse(
    val isTaken: Boolean
) {

    fun asBoolean(): Boolean = isTaken
}