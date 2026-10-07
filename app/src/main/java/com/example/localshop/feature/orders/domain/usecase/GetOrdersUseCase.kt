package com.example.localshop.feature.orders.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.model.Order
import com.example.localshop.feature.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<ResultState<List<Order>>> {
        return orderRepository.getOrders()
    }
}
