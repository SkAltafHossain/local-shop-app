package com.example.localshop.feature.orders.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<ResultState<List<Order>>>
    fun getOrderDetails(orderId: Int): Flow<ResultState<Order>>
    fun confirmDelivered(orderId: Int): Flow<ResultState<Unit>>
}
