package com.example.localshop.feature.auth.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.auth.domain.model.User
import com.example.localshop.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(name: String?, email: String?, phone: String?): Flow<ResultState<User>> {
        return repository.updateUser(name, email, phone)
    }
}