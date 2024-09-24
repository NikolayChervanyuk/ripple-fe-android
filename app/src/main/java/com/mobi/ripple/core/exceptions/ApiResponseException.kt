package com.mobi.ripple.core.exceptions

import io.ktor.http.HttpStatusCode

class ApiResponseException(
    statusCode: HttpStatusCode,
    errorMessage: String? = null,
) : RuntimeException("API responded with error $statusCode ${errorMessage?: ""}") {
}