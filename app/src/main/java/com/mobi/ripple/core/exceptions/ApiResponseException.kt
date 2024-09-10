package com.mobi.ripple.core.exceptions

import io.ktor.http.HttpStatusCode

class ApiResponseException(
    val statusCode: HttpStatusCode,
    val errorMessage: String = "",
) : RuntimeException("API responded with error $statusCode $errorMessage") {
}