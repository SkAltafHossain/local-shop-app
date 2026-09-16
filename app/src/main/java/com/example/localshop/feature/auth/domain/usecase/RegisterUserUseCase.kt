package com.example.localshop.feature.auth.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.model.AuthResponse
import com.example.localshop.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(name: String, email: String, password: String, passwordConfirmation: String): Flow<ResultState<AuthResponse>> {
        return repository.register(name, email, password, passwordConfirmation)
    }
}