package com.example.localshop.feature.checkout.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequestDto(
    @SerialName("address_id")
    val addressId: Int? = null,
    
    @SerialName("payment_method")
    val paymentMethod: String,
    
    @SerialName("items")
    val items: List<CheckoutItemDto>
)

@Serializable
data class CheckoutItemDto(
    @SerialName("product_id")
    val productId: Int,
    
    @SerialName("quantity")
    val quantity: Int
)
