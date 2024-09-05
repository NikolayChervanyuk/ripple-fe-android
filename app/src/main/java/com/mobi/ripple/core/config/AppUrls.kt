package com.mobi.ripple.core.config

sealed interface AppUrls {
    companion object {
        const val BASE_URL = "http://192.168.1.5:8080/"
    }

    object AuthUrls {
        const val REGISTER_URL: String = "auth/register"
        const val LOGIN_URL: String = "auth/login"
        const val REFRESH_TOKENS_URL: String = "auth/refresh-token"
        const val LOGOUT_URL: String = "auth/logout"
        const val USERS = "users"
    }

    object ProfileUrls {
        private const val USER = "user"
        private const val USERS_OTHER = "users/other"
        const val USER_PROFILE_PICTURE = "user-pfp"
        fun userProfilePicture(username: String) = "$USER_PROFILE_PICTURE/$username"
        fun userProfileInfoUrl(username: String) = "$USER/$username"
        fun existsOtherUserWithUsername(username: String) = "$USERS_OTHER?username=$username"
        fun existsOtherUserWithEmail(email: String) = "$USERS_OTHER?email=$email"
        const val POST = "/p"

        const val UPDATE_USER = USER

        fun userSimplePostsUrl(username: String) = "$USER/$username/p"
    }

    object SearchUrls {
        private const val USERS = "users"
        private const val FIND_SIMPLE = "find-simple"
        fun searchUsersLike(username: String) = "$USERS/$FIND_SIMPLE?like=$username"
    }
}