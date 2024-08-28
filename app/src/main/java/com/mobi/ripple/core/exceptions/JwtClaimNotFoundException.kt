package com.mobi.ripple.core.exceptions

import io.jsonwebtoken.JwtException

class JwtClaimNotFoundException(
    override val message: String = "A jwt claim was not found",
    cause: Throwable = Throwable()
) :
    JwtException(message, cause) {
}