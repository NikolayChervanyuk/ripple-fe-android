package com.mobi.ripple.core.util.validator

import com.mobi.ripple.core.util.validator.FieldValidator.Companion.MAX_USERNAME_LENGTH
import com.mobi.ripple.core.util.validator.FieldValidator.Companion.MIN_USERNAME_LENGTH

class UsernameValidator {
    companion object {
        fun isValid(username: String): Boolean {
            if (!hasLength(username)) return false

            if (!notStartOrEndWithComma(username)) return false

            var hasLetterOrDigit = false
            var isPrevSeparator = false

            val usernameLowercase = username.lowercase()
            for (i in usernameLowercase.indices) {
                if (usernameLowercase[i] == '.' || usernameLowercase[i] == '_') {
                    if (isPrevSeparator) return false
                    isPrevSeparator = true
                    continue
                }
                if (usernameLowercase[i].isLetterOrDigit()) {
                    hasLetterOrDigit = true
                    isPrevSeparator = false
                    continue
                }
                return false
            }
            return hasLetterOrDigit
        }

        fun hasLength(username: String): Boolean {
            return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH
        }

        fun notStartOrEndWithComma(username: String): Boolean {
            if (username.isEmpty()) return false
            return !(username[0] == '.' || username[username.length - 1] == '.')
        }

        fun hasNoConsequentSeparators(username: String): Boolean {
            if (username.isEmpty()) return false
            var isSeparator = false
            for (i in username.lowercase().indices) {
                if (isSeparator && username[i].isSeparator()) return false
                isSeparator = username[i].isSeparator()
            }
            return true
        }

        fun hasAllowedCharacterOnly(username: String): Boolean {
            if(username.isEmpty()) return false
            for (i in username.lowercase().indices) {
                if(!username[i].isLetterOrDigit() && !username[i].isSeparator()) return false
            }
            return true
        }

        private fun Char.isSeparator(): Boolean {
            return this == '.' || this == '_'
        }
    }
}