package com.example.localshop.feature.cart.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<ResultState<Cart>> {
        return cartRepository.getCart()
    }
}
