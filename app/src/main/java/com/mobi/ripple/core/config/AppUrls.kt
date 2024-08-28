package com.mobi.ripple.core.config

sealed interface AppUrls {
    companion object {
        const val BASE_URL = "http://192.168.0.123:8080/"
    }

    object AuthUrls {
        const val REGISTER_URL: String = "auth/register"
        const val LOGIN_URL: String = "auth/login"
        const val REFRESH_TOKENS_URL: String = "auth/refresh-token"
        const val USERS = "users"
    }

    object ProfileUrls {
        private const val USER = "user"
        fun userProfilePicture(username: String) = "user-pfp/$username"
        fun userProfileInfoUrl(username: String) = "$USER/$username"
        fun userSimplePostsUrl(username: String) = "$USER/$username/p"
    }
}