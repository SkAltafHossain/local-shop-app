package com.example.localshop.feature.cart.data.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.cart.data.mapper.BuyNowMapper
import com.example.localshop.feature.cart.data.mapper.CartMapper
import com.example.localshop.feature.cart.data.remote.CartApi
import com.example.localshop.feature.cart.data.remote.dto.BuyNowRequestDto
import com.example.localshop.feature.cart.data.remote.dto.CartRequestDto
import com.example.localshop.feature.cart.data.remote.dto.CartUpdateRequestDto
import com.example.localshop.feature.cart.domain.model.BuyNowData
import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.cart.domain.model.CartItem
import com.example.localshop.feature.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi
) : CartRepository {

    private fun <T> extractErrorMessageFromResponse(response: com.example.localshop.core.network.ApiResponse<T>): String {
        // If message is generic like "Validation failed", try to get specific errors
        if (response.message == "Validation failed" || response.message == "Validation error") {
            if (response.errors != null) {
                for ((field, errorElement) in response.errors) {
                    when (errorElement) {
                        is JsonArray -> {
                            // Handle array of error messages - take first one
                            if (errorElement.isNotEmpty()) {
                                val firstError = errorElement[0]
                                if (firstError is JsonPrimitive) {
                                    return firstError.content
                                }
                            }
                        }
                        else -> {
                            // Handle single error message as string
                            if (errorElement is JsonPrimitive) {
                                return errorElement.content
                            }
                        }
                    }
                }
            }
        }
        return response.message ?: "Operation failed"
    }

    override fun addToCart(productId: Int, quantity: Int): Flow<ResultState<CartItem>> = flow {
        emit(ResultState.Loading)
        try {
            val request = CartRequestDto(productId, quantity)
            val response = cartApi.addToCart(request)
            if (response.success && response.data != null) {
                val cartItem = CartMapper.toDomainFromAddToCart(response.data)
                emit(ResultState.Success(cartItem))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to add item to cart"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun getCart(): Flow<ResultState<Cart>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.getCart()
            if (response.success && response.data != null) {
                val cart = CartMapper.toDomain(response.data)
                emit(ResultState.Success(cart))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to fetch cart"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun updateCartItem(cartItemId: Int, quantity: Int): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val request = CartUpdateRequestDto(quantity)
            val response = cartApi.updateCartItem(cartItemId, request)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to update cart item"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun deleteCartItem(cartItemId: Int): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.deleteCartItem(cartItemId)
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to delete cart item"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun clearCart(): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = cartApi.clearCart()
            if (response.success) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Failed to clear cart"
            emit(ResultState.Error(errorMessage))
        }
    }

    override fun buyNow(productId: Int, quantity: Int): Flow<ResultState<BuyNowData>> = flow {
        emit(ResultState.Loading)
        try {
            val request = BuyNowRequestDto(productId, quantity)
            android.util.Log.d("BuyNow", "Request: $request")
            val response = cartApi.buyNow(request)
            android.util.Log.d("BuyNow", "Raw Response: success=${response.success}, message=${response.message}, data=${response.data != null}")
            if (response.success && response.data != null) {
                try {
                    val buyNowData = BuyNowMapper.toDomain(response.data)
                    android.util.Log.d("BuyNow", "Mapping successful: ${buyNowData.product.name}")
                    emit(ResultState.Success(buyNowData))
                } catch (e: Exception) {
                    android.util.Log.e("BuyNow", "Mapping error", e)
                    android.util.Log.e("BuyNow", "Response data: ${response.data}")
                    emit(ResultState.Error("Mapping error: ${e.message}"))
                }
            } else {
                val errorMessage = extractErrorMessageFromResponse(response)
                android.util.Log.e("BuyNow", "API error: $errorMessage")
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("BuyNow", "Network error", e)
            val errorMessage = e.message ?: "Failed to process Buy Now"
            emit(ResultState.Error(errorMessage))
        }
    }
}
