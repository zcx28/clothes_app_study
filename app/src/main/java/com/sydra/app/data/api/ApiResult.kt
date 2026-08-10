package com.sydra.app.data.api

enum class ApiErrorCode {
    UNAUTHENTICATED,
    FORBIDDEN,
    INVALID_ARGUMENT,
    NOT_FOUND,
    CONFLICT,
    OUT_OF_STOCK,
    ADDRESS_INVALID,
    ORDER_NOT_ELIGIBLE,
    UPLOAD_FAILED,
    NETWORK,
    UNKNOWN
}

data class ApiError(
    val code: ApiErrorCode,
    val message: String,
    val retryable: Boolean = false,
    val fieldErrors: Map<String, String> = emptyMap()
)

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>

    data class Failure(val error: ApiError) : ApiResult<Nothing>
}
