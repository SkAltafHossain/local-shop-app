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
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) : AuthRepository {
    
    override fun register(name: String, email: String, password: String, passwordConfirmation: String): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val request = RegisterRequestDto(name, email, password, passwordConfirmation)
            val response = authApi.register(request)
            if (response.success && response.data != null) {
                val authResponse = AuthMapper.mapToDomain(response.data)
                tokenProvider.saveToken(authResponse.token)
                emit(ResultState.Success(authResponse))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
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
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            tokenProvider.clearToken()
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
}