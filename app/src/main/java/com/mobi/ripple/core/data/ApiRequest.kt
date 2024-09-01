package com.mobi.ripple.core.data

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import timber.log.Timber

@Serializable
class ApiRequest<C>(
    @Transient val nullRequestReason: String =
        "No request was made because provided 'httpResponseFunction' returned null",
    @Transient val httpResponseFunction: (suspend () -> HttpResponse?)? = null
) {

    var data: C? = null
    var errorMessage: String? = null

    suspend inline fun <reified T : C> sendRequest(): ApiResponse<T> {


        try {
            val httpResponse = httpResponseFunction!!.invoke()
                ?: return ApiResponse(
                    content = null,
                    errorMessage = nullRequestReason,
                    httpStatusCode = HttpStatusCode.BadRequest,
                    isError = true
                )
            val statusCode = httpResponse.status

            if(statusCode == HttpStatusCode.NotFound) {
                return ApiResponse(
                    content = null,
                    errorMessage = HttpStatusCode.NotFound.description,
                    httpStatusCode = HttpStatusCode.NotFound,
                    isError = true
                )
            }

            val isError = statusCode.value > 299
            val parsedResponse = httpResponse.body<ApiRequest<T>>()
            return ApiResponse(
                content = if (!isError) parsedResponse.data as T else null,
                errorMessage = if (isError) parsedResponse.errorMessage else null,
                httpStatusCode = statusCode,
                isError = isError
            )
        } catch (e: Exception) {
            Timber.e("Application error: ${e.message}\n${e.printStackTrace()}")
            return ApiResponse(
                content = null,
                errorMessage = e.message,
                httpStatusCode = HttpStatusCode.UnprocessableEntity,
                isError = true
            )
        }

    }
}