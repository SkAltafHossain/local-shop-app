package com.example.localshop.feature.cart.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.domain.model.BuyNowData
import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.cart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun addToCart(productId: Int, quantity: Int): Flow<ResultState<CartItem>>
    fun getCart(): Flow<ResultState<Cart>>
    fun updateCartItem(cartItemId: Int, quantity: Int): Flow<ResultState<Unit>>
    fun deleteCartItem(cartItemId: Int): Flow<ResultState<Unit>>
    fun clearCart(): Flow<ResultState<Unit>>
    fun buyNow(productId: Int, quantity: Int): Flow<ResultState<BuyNowData>>
}
