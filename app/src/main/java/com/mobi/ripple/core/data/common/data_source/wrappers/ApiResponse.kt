package com.mobi.ripple.core.data.common.data_source.wrappers

import com.mobi.ripple.core.domain.common.Response
import io.ktor.http.HttpStatusCode

data class ApiResponse<C>(
    val content: C?,
    val errorMessage: String?,
    val httpStatusCode: HttpStatusCode,
    val isError: Boolean
){
    fun <T> toResponse(transformedContent: T?): Response<T> {
        return Response(
            transformedContent,
            errorMessage ?: "",
            httpStatusCode,
            isError
        )
    }
}