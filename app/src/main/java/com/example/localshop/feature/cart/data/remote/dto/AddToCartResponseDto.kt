package com.example.localshop.feature.cart.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartResponseDto(
    @SerialName("cart_count")
    val cartCount: Int,
    
    @SerialName("cart_item")
    val cartItem: AddToCartItemDto
)

@Serializable
data class AddToCartItemDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("user_id")
    val userId: Int,
    
    @SerialName("product_id")
    val productId: Int,
    
    @SerialName("quantity")
    val quantity: Int,
    
    @SerialName("created_at")
    val createdAt: String,
    
    @SerialName("updated_at")
    val updatedAt: String
)