package com.example.localshop.feature.cart.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(cartItemId: Int): Flow<ResultState<Unit>> {
        return cartRepository.deleteCartItem(cartItemId)
    }
}
