package com.example.localshop.core.error

sealed class AppError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    
    data class NetworkError(
        val userMessage: String,
        val errorCause: Throwable? = null
    ) : AppError(userMessage, errorCause ?: RuntimeException(userMessage))
    
    data class HttpError(
        val code: Int,
        val userMessage: String,
        val errorCause: Throwable? = null
    ) : AppError("HTTP $code: $userMessage", errorCause)
    
    data class ValidationError(
        val userMessage: String,
        val errors: Map<String, List<String>> = emptyMap()
    ) : AppError(userMessage)
    
    data class UnauthorizedError(
        val userMessage: String = "Unauthorized access"
    ) : AppError(userMessage)
    
    data class ForbiddenError(
        val userMessage: String = "Access forbidden"
    ) : AppError(userMessage)
    
    data class NotFoundError(
        val userMessage: String = "Resource not found"
    ) : AppError(userMessage)
    
    data class ServerError(
        val userMessage: String = "Server error occurred"
    ) : AppError(userMessage)
    
    data class UnknownError(
        val userMessage: String,
        val errorCause: Throwable? = null
    ) : AppError(userMessage, errorCause)
    
    data class TimeoutError(
        val userMessage: String = "Request timeout"
    ) : AppError(userMessage)
    
    data class NoInternetError(
        val userMessage: String = "No internet connection"
    ) : AppError(userMessage)
}