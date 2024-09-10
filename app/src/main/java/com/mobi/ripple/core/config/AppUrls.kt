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

        fun followOrUnfollowUser(username: String) = "$USER/$username/follow"

        fun getFollowers(username: String, page: Int) = "$USER/$username/followers?page=$page"
        fun getFollowing(username: String, page: Int) = "$USER/$username/following?page=$page"
    }

    object SearchUrls {
        private const val USERS = "users"
        private const val FIND_SIMPLE = "find-simple"
        fun searchUsersLike(username: String) = "$USERS/$FIND_SIMPLE?like=$username"
    }

    object PostUrls {

        private const val USER = "user"
        private const val POST = "p"
        fun getSimpleUser(userId: String) = "$USER?id=$userId"
        fun getPost(postId: String) = "$POST/$postId"
        fun getPosts(authorId: String, page: Int) = "$POST?authorId=$authorId&page=$page"
        fun likeOrUnlikePost(postId: String) = "$POST/$postId/like"
        fun getPostComments(postId: String, page: Int) = "$POST/$postId/comments?page=$page"
        fun uploadPostComment(postId: String) = "$POST/$postId/comments"
    }

}