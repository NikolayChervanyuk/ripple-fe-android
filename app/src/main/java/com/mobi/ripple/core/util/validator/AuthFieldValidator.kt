package com.mobi.ripple.core.util.validator

import android.util.Patterns

class AuthFieldValidator {
    companion object {

        const val MAX_FULLNAME_LENGTH = 60
        const val MAX_USERNAME_LENGTH = 40
        const val MIN_USERNAME_LENGTH = 2

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

        /**
         * Uses directly [PasswordValidator.isValid] to validate and can be used interchangeably.
         */
        fun isPasswordValid(password: String): Boolean =
            PasswordValidator.isValid(password)


    }
}