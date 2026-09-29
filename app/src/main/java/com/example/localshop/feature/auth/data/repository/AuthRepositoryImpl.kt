package com.example.localshop.feature.auth.data.repository

import com.example.localshop.core.error.ErrorMapper
import com.example.localshop.core.network.TokenProvider
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.data.mapper.AuthMapper
import com.example.localshop.feature.auth.data.remote.AuthApi
import com.example.localshop.feature.auth.data.remote.dto.ForgotPasswordRequestDto
import com.example.localshop.feature.auth.data.remote.dto.LoginRequestDto
import com.example.localshop.feature.auth.data.remote.dto.RegisterRequestDto
import com.example.localshop.feature.auth.data.remote.dto.ResetPasswordRequestDto
import com.example.localshop.feature.auth.domain.model.AuthResponse
import com.example.localshop.feature.auth.domain.model.User
import com.example.localshop.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) : AuthRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun extractErrorMessage(exception: Exception): String {
        if (exception is HttpException) {
            val errorBody = exception.response()?.errorBody()
            if (errorBody != null) {
                try {
                    val errorJson = errorBody.string()
                    val jsonObject = json.parseToJsonElement(errorJson).jsonObject

                    // First check for message field
                    val message = jsonObject["message"]?.jsonPrimitive?.content
                    if (!message.isNullOrBlank()) {
                        // If message is generic like "Validation failed", try to get specific errors
                        if (message == "Validation failed" || message == "Validation error") {
                            val errors = extractErrorMessages(jsonObject)
                            if (errors.isNotEmpty()) {
                                return errors.first()
                            }
                        }
                        return message
                    }

                    // If no message, try to extract from errors
                    val errors = extractErrorMessages(jsonObject)
                    if (errors.isNotEmpty()) {
                        return errors.first()
                    }
                } catch (e: Exception) {
                    // If parsing fails, fall back to default error
                }
            }
        }
        val appError = ErrorMapper.mapToAppError(exception)
        return appError.localizedMessage ?: appError.toString()
    }

    private fun extractErrorMessages(jsonObject: JsonObject): List<String> {
        val errorsObject = jsonObject["errors"]?.jsonObject
        if (errorsObject != null) {
            val errorMessages = mutableListOf<String>()
            for ((field, errorElement) in errorsObject) {
                when (errorElement) {
                    is JsonArray -> {
                        // Handle array of error messages - take first one
                        if (errorElement.isNotEmpty()) {
                            val errorMsg = errorElement[0].jsonPrimitive.content
                            errorMessages.add(errorMsg)
                        }
                    }
                    else -> {
                        // Handle single error message as string
                        val errorMsg = errorElement.jsonPrimitive.content
                        errorMessages.add(errorMsg)
                    }
                }
            }
            return errorMessages
        }
        return emptyList()
    }

    private fun <T> extractErrorMessageFromResponse(response: com.example.localshop.core.network.ApiResponse<T>): String {
        // If message is generic like "Validation failed", try to get specific errors
        if (response.message == "Validation failed" || response.message == "Validation error") {
            if (response.errors != null) {
                for ((field, errorElement) in response.errors) {
                    when (errorElement) {
                        is kotlinx.serialization.json.JsonArray -> {
                            // Handle array of error messages - take first one
                            if (errorElement.isNotEmpty()) {
                                return errorElement[0].jsonPrimitive.content
                            }
                        }
                        else -> {
                            // Handle single error message as string
                            return errorElement.jsonPrimitive.content
                        }
                    }
                }
            }
        }
        return response.message ?: "Operation failed"
    }
    
    override fun register(name: String, email: String, phone: String, password: String, passwordConfirmation: String): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val request = RegisterRequestDto(name, email, password, passwordConfirmation, phone)
            val response = authApi.register(request)
            if (response.success && response.data != null) {
                val authResponse = AuthMapper.mapToDomain(response.data)
                tokenProvider.saveToken(authResponse.token)
                emit(ResultState.Success(authResponse))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun login(email: String, password: String): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val request = LoginRequestDto(email, password)
            val response = authApi.login(request)
            if (response.success && response.data != null) {
                val authResponse = AuthMapper.mapToDomain(response.data)
                tokenProvider.saveToken(authResponse.token)
                emit(ResultState.Success(authResponse))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun forgotPassword(email: String): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val request = ForgotPasswordRequestDto(email)
            val response = authApi.forgotPassword(request)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun resetPassword(email: String, token: String, password: String, passwordConfirmation: String): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val request = ResetPasswordRequestDto(email, token, password, passwordConfirmation)
            val response = authApi.resetPassword(request)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun getCurrentUser(): Flow<ResultState<User>> = flow {
        emit(ResultState.Loading)
        try {
            val response = authApi.getCurrentUser()
            if (response.success && response.data != null) {
                val user = AuthMapper.mapToDomain(response.data)
                emit(ResultState.Success(user))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun updateUser(name: String?, email: String?, phone: String?): Flow<ResultState<User>> = flow {
        emit(ResultState.Loading)
        try {
            val request = mutableMapOf<String, String>()
            name?.let { request["name"] = it }
            email?.let { request["email"] = it }
            phone?.let { request["phone"] = it }

            val response = authApi.updateUser(request)
            if (response.success && response.data != null) {
                val user = AuthMapper.mapToDomain(response.data)
                emit(ResultState.Success(user))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun updatePassword(currentPassword: String, newPassword: String, newPasswordConfirmation: String): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val request = mapOf(
                "current_password" to currentPassword,
                "password" to newPassword,
                "password_confirmation" to newPasswordConfirmation
            )
            val response = authApi.updatePassword(request)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
    
    override fun logout(): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = authApi.logout()
            tokenProvider.clearToken()
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            tokenProvider.clearToken()
            val errorMessage = extractErrorMessage(e)
            emit(ResultState.Error(errorMessage))
        }
    }
}