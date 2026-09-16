package com.example.localshop.feature.auth.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.model.AuthResponse
import com.example.localshop.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(name: String, email: String, password: String, passwordConfirmation: String): Flow<ResultState<AuthResponse>>
    fun login(email: String, password: String): Flow<ResultState<AuthResponse>>
    fun forgotPassword(email: String): Flow<ResultState<Unit>>
    fun resetPassword(email: String, token: String, password: String, passwordConfirmation: String): Flow<ResultState<Unit>>
    fun getCurrentUser(): Flow<ResultState<User>>
    fun updateUser(name: String?, email: String?, phone: String?): Flow<ResultState<User>>
    fun updatePassword(currentPassword: String, newPassword: String, newPasswordConfirmation: String): Flow<ResultState<Unit>>
    fun logout(): Flow<ResultState<Unit>>
}