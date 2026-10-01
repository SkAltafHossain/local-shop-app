package com.example.localshop.feature.address.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.address.domain.model.Address
import com.example.localshop.feature.address.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    operator fun invoke(addressId: Int, address: Address): Flow<ResultState<Address>> {
        return repository.updateAddress(addressId, address)
    }
}
