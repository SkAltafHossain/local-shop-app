package com.example.localshop.feature.address.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.address.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetDefaultAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    operator fun invoke(addressId: Int): Flow<ResultState<Unit>> {
        return repository.setDefaultAddress(addressId)
    }
}
