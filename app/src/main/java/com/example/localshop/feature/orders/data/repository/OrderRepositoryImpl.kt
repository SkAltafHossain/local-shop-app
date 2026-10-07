package com.example.localshop.feature.orders.data.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.data.remote.CartApi
import com.example.localshop.feature.orders.data.mapper.OrderMapper
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val cartApi: CartApi
) : OrderRepository {

    private fun <T> extractErrorMessageFromResponse(response: com.example.localshop.core.network.ApiResponse<T>): String {
        if (response.message == "Validation failed" || response.message == "Validation error") {
            if (response.errors != null) {
                for ((field, errorElement) in response.errors) {
                    when (errorElement) {
                        is kotlinx.serialization.json.JsonArray -> {
                            if (errorElement.isNotEmpty()) {
                                val firstError = errorElement[0]
                                if (firstError is kotlinx.serialization.json.JsonPrimitive) {
                                    return firstError.content
                                }
                            }
                        }
                        else -> {
                            if (errorElement is kotlinx.serialization.json.JsonPrimitive) {
                                return errorElement.content
                            }
                        }
                    }
                }
            }
        }
        return response.message ?: "Operation failed"
    }

    override fun getOrders(): Flow<ResultState<List<Order>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.getOrders()
            if (response.success && response.data != null) {
                val orders = OrderMapper.toDomainList(response.data.orders)
                emit(ResultState.Success(orders))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to fetch orders"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun getOrderDetails(orderId: Int): Flow<ResultState<Order>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.getOrderDetails(orderId)
            if (response.success && response.data != null) {
                val order = OrderMapper.toDomain(response.data)
                emit(ResultState.Success(order))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to fetch order details"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun confirmDelivered(orderId: Int): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.confirmDelivered(orderId)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to confirm delivery"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun downloadBill(orderId: Int): Flow<ResultState<okhttp3.ResponseBody>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.downloadBill(orderId)
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error("Failed to download bill"))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to download bill"
            emit(ResultState.Error(errorMessage))
        }
    }
}
