package com.mobi.ripple.core.domain

import io.ktor.http.HttpStatusCode

data class Response<C>(
    val content: C?,
    val errorMessage: String,
    val httpStatusCode: HttpStatusCode,
    val isError: Boolean
)