package com.mobi.ripple.core.util.validator

import android.util.Patterns

class FieldValidator {
    companion object {

        const val MAX_FULLNAME_LENGTH = 60

        const val MAX_USERNAME_LENGTH = 40
        const val MIN_USERNAME_LENGTH = 2

        const val MIN_COMMENT_LENGTH = 1
        const val MAX_COMMENT_LENGTH = 4096

        const val MIN_MESSAGE_LENGTH = 1
        const val MAX_MESSAGE_LENGTH = 4096

        const val MIN_CHAT_NAME_LENGTH = 0
        const val MAX_CHAT_NAME_LENGTH = 80


        fun isFullNameValid(fullName: String): Boolean {
            return fullName.length <= MAX_FULLNAME_LENGTH
        }

        /** A valid username should:
         * 1. Not start or end with a comma
         * 2. Not have 2 or more separators(comma or underscore) after one another
         * 3. Have at least 1 letter or digit
         * 4. Be at least 2 characters long
         * */
        fun isUsernameValid(username: String): Boolean {
            return UsernameValidator.isValid(username)
        }

        fun isEmailValid(email: String): Boolean =
            Patterns.EMAIL_ADDRESS.matcher(email).matches();

        fun isPasswordValid(password: String): Boolean =
            PasswordValidator.isValid(password)

        fun isCommentValid(comment: String): Boolean {
            return comment.length in MIN_COMMENT_LENGTH..MAX_COMMENT_LENGTH &&
                    comment.isNotBlank()
        }

        fun isChatNameValid(chatName: String): Boolean {
            return chatName.length in MIN_CHAT_NAME_LENGTH..MAX_CHAT_NAME_LENGTH
        }

        fun isChatMessageValid(message: String): Boolean {
            return message.length in MIN_MESSAGE_LENGTH..MAX_MESSAGE_LENGTH && message.isNotBlank()
        }
    }
}