package com.example.localshop.feature.auth.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, token: String, password: String, passwordConfirmation: String): Flow<ResultState<Unit>> {
        return repository.resetPassword(email, token, password, passwordConfirmation)
    }
}