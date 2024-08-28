package com.mobi.ripple.core.util.validator

class PasswordValidator {
    companion object {
        object PasswordRequirements {
            const val MIN_CHAR_LENGTH = 6
            const val SHOULD_HAVE_LOWERCASE = true
            const val SHOULD_HAVE_UPPERCASE = true
            const val SHOULD_HAVE_DIGIT = true
        }

        fun isValid(
            passwordString: String,
            requiredLength: Int? = PasswordRequirements.MIN_CHAR_LENGTH
        ): Boolean {
            return (
                    (if (PasswordRequirements.SHOULD_HAVE_LOWERCASE) {
                        hasLowerCase(passwordString)
                    } else true) &&
                            (if (PasswordRequirements.SHOULD_HAVE_UPPERCASE) {
                                hasUpperCase(passwordString)
                            } else true) &&
                            (if (PasswordRequirements.SHOULD_HAVE_DIGIT) {
                                hasDigit(passwordString)
                            } else true) &&
                            (requiredLength?.let { hasLength(passwordString, it) } ?: true)
                    )
        }

        fun hasLength(
            passwordString: String,
            requiredLength: Int = PasswordRequirements.MIN_CHAR_LENGTH
        ): Boolean {
            return passwordString.length >= requiredLength
        }

        fun hasLowerCase(passwordString: String): Boolean {
            passwordString.forEach { if (it.isLowerCase()) return true }
            return false
        }

        fun hasUpperCase(passwordString: String): Boolean {
            passwordString.forEach { if (it.isUpperCase()) return true }
            return false
        }

        fun hasDigit(passwordString: String): Boolean {
            passwordString.forEach { if (it.isDigit()) return true }
            return false
        }
    }
}