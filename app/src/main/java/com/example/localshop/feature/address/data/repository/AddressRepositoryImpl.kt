package com.example.localshop.feature.address.data.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.address.data.mapper.AddressMapper
import com.example.localshop.feature.address.data.remote.AddressApi
import com.example.localshop.feature.address.domain.model.Address
import com.example.localshop.feature.address.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApi: AddressApi
) : AddressRepository {

    private fun <T> extractErrorMessageFromResponse(response: com.example.localshop.core.network.ApiResponse<T>): String {
        return response.message ?: "Operation failed"
    }

    override fun getAddresses(): Flow<ResultState<List<Address>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = addressApi.getAddresses()
            if (response.success && response.data != null) {
                val addresses = response.data.map { AddressMapper.toDomain(it) }
                    .sortedBy { it.id }
                emit(ResultState.Success(addresses))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to load addresses"))
        }
    }

    override fun addAddress(address: Address): Flow<ResultState<Address>> = flow {
        emit(ResultState.Loading)
        try {
            val request = AddressMapper.toRequestDto(address)
            val response = addressApi.addAddress(request)
            if (response.success && response.data != null) {
                val newAddress = AddressMapper.toDomain(response.data)
                emit(ResultState.Success(newAddress))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to load addresses"))
        }
    }

    override fun updateAddress(addressId: Int, address: Address): Flow<ResultState<Address>> = flow {
        emit(ResultState.Loading)
        try {
            val request = AddressMapper.toRequestDto(address)
            val response = addressApi.updateAddress(addressId, request)
            if (response.success && response.data != null) {
                val updatedAddress = AddressMapper.toDomain(response.data)
                emit(ResultState.Success(updatedAddress))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to load addresses"))
        }
    }

    override fun deleteAddress(addressId: Int): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = addressApi.deleteAddress(addressId)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to load addresses"))
        }
    }

    override fun setDefaultAddress(addressId: Int): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = addressApi.setDefaultAddress(addressId)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Failed to load addresses"))
        }
    }
}
