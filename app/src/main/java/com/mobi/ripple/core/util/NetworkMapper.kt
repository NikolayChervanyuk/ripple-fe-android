package com.mobi.ripple.core.util

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException

fun Throwable.toNetworkError(): NetworkError {
    val error = when (this) {
        is IOException -> ApiError.NetworkError
        is RedirectResponseException -> ApiError.RedirectResponseError
        is ClientRequestException -> ApiError.ClientRequestError
        is ServerResponseException -> ApiError.ServerResponseError
        else -> ApiError.UnknownError
    }

    return NetworkError(error = error, t = this)
}