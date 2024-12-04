package com.mobi.ripple.core.exceptions

class StoredUsernameNotFoundException(
    override val message: String = "Stored username not found",
) : RuntimeException() {
}