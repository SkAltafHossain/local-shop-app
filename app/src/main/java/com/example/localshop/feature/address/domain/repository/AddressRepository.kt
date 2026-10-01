package com.example.localshop.feature.address.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.address.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getAddresses(): Flow<ResultState<List<Address>>>
    fun addAddress(address: Address): Flow<ResultState<Address>>
    fun updateAddress(addressId: Int, address: Address): Flow<ResultState<Address>>
    fun deleteAddress(addressId: Int): Flow<ResultState<Unit>>
    fun setDefaultAddress(addressId: Int): Flow<ResultState<Unit>>
}
