package com.example.localshop.feature.cart.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.model.CartItem
import com.example.localshop.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(productId: Int, quantity: Int = 1): Flow<ResultState<CartItem>> {
        return cartRepository.addToCart(productId, quantity)
    }
}
