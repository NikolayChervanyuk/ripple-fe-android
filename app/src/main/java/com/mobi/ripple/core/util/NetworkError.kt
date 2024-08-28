package com.mobi.ripple.core.util

data class NetworkError(
    val error: ApiError,
    val t: Throwable? = null,
    val rootErrorMessage: String = t?.message ?: error.message
)

enum class ApiError(val message: String) {
    NetworkError("Network error"),
    RedirectResponseError("Redirect response error (3xx)"),
    ClientRequestError("Client request error (4xx)"),
    ServerResponseError("Server response error (5xx)"),
    UnknownError("Unknown error")

}
