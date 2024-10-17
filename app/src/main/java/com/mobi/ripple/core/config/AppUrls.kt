package com.mobi.ripple.core.config

sealed interface AppUrls {
    companion object {
        const val PORT = 8080
        const val HOST = "192.168.1.2"
        const val BASE_URL = "http://$HOST:$PORT/"
        const val WEBSOCKET_MESSAGES_PATH = "chat-ws-messages"
    }

    object AuthUrls {
        const val REGISTER_URL: String = "auth/register"
        const val LOGIN_URL: String = "auth/login"
        const val REFRESH_TOKENS_URL: String = "auth/refresh-token"
        const val LOGOUT_URL: String = "auth/logout"
        const val USERS = "users"

        fun getSimpleAuthUser(username: String) = "user?username=$username"
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
        fun searchUsersLike(username: String) = "$USERS/$FIND_SIMPLE?username=$username"
    }

    object PostUrls {
        private const val USER = "user"
        private const val POST = "p"

        fun getSimpleUser(userId: String) = "$USER?id=$userId"

        fun getPost(postId: String) = "$POST/$postId"
        fun getPosts(authorId: String, page: Int) = "$POST?authorId=$authorId&page=$page"
        fun likeOrUnlikePost(postId: String) = "$POST/$postId/like"
        fun getPostComments(postId: String, page: Int) = "$POST/$postId/comments?page=$page"

        private fun postCommentsRoute(postId: String) = "$POST/$postId/comments"

        fun uploadPostComment(postId: String) = postCommentsRoute(postId)
        fun likeOrUnlikeComment(postId: String, commentId: String) =
            "${postCommentsRoute(postId)}/$commentId/like"

        fun editDeleteComment(postId: String, commentId: String) =
            "${postCommentsRoute(postId)}?id=$commentId"

        private fun commentRepliesRoute(postId: String, commentId: String) =
            "${postCommentsRoute(postId)}/$commentId/replies"

        fun getLatestReplies(postId: String, commentId: String, page: Int) =
            "${commentRepliesRoute(postId, commentId)}?page=$page"

        fun uploadCommentReply(postId: String, commentId: String) =
            commentRepliesRoute(postId, commentId)

        fun likeOrUnlikeReply(postId: String, commentId: String, replyId: String) =
            "${commentRepliesRoute(postId, commentId)}/$replyId/like"

        fun editDeleteReply(postId: String, commentId: String, replyId: String) =
            "${commentRepliesRoute(postId, commentId)}?id=$replyId"
    }

    object ChatUrls {
        private const val USER = "user"
        private const val CHAT = "chat"

        const val HAS_PENDING_MESSAGES = "$CHAT/has-pending"

        /**
         * @exception IllegalArgumentException If both parameters are null
         */
        fun findSimpleUsersLike(fullName: String?, username: String?): String {
            if (fullName == null && username == null) {
                throw IllegalArgumentException("FullName and username can't be both null")
            }
            val fullNameParam = fullName?.let {
                if (it.isNotBlank()) "fullName=$fullName"
                else null
            } ?: ""

            val usernameParam = username?.let {
              if (it.isNotBlank()) "username=$username"
              else null
            } ?: ""

            val and = if (fullNameParam != ""  && usernameParam != "") "&" else ""

            return "users/find-simple?$fullNameParam$and$usernameParam"
        }

        const val CREATE_CHAT = CHAT

        fun getSimpleChatUser(userId: String) = "${USER}?id=$userId"

        fun getChatParticipants(chatId: String) = "$CHAT/$chatId/participants"

        fun getChats(page: Int) = "$CHAT?page=$page"

        fun getChatMessages(chatId: String, page: Int) = "$CHAT/$chatId/messages?page=$page"
    }
}