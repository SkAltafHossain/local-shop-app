package com.example.localshop.feature.auth.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.model.AuthResponse
import com.example.localshop.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<ResultState<AuthResponse>> {
        return repository.login(email, password)
    }
}