package com.example.localshop.feature.orders.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfirmDeliveredUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(orderId: Int): Flow<ResultState<Unit>> {
        return orderRepository.confirmDelivered(orderId)
    }
}
