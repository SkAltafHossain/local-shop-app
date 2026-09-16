package com.example.localshop.core.error

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorMapper {
    fun mapToAppError(throwable: Throwable): AppError {
        return when (throwable) {
            is AppError -> throwable
            is SocketTimeoutException -> AppError.TimeoutError()
            is UnknownHostException -> AppError.NoInternetError()
            is IOException -> AppError.NetworkError(
                userMessage = "Network error: ${throwable.message}",
                errorCause = throwable
            )
            else -> AppError.UnknownError(
                userMessage = throwable.message ?: "An unknown error occurred",
                errorCause = throwable
            )
        }
    }
    
    fun mapHttpCodeToError(code: Int, message: String = ""): AppError {
        return when (code) {
            400 -> AppError.HttpError(code, "Bad request: $message")
            401 -> AppError.UnauthorizedError(message.ifEmpty { "Unauthorized access" })
            403 -> AppError.ForbiddenError(message.ifEmpty { "Access forbidden" })
            404 -> AppError.NotFoundError(message.ifEmpty { "Resource not found" })
            422 -> AppError.ValidationError(message.ifEmpty { "Validation failed" })
            in 500..599 -> AppError.ServerError(message.ifEmpty { "Server error occurred" })
            else -> AppError.HttpError(code, message.ifEmpty { "HTTP error: $code" })
        }
    }
}