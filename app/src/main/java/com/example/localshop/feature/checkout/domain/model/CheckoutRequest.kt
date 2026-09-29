package com.example.localshop.feature.checkout.domain.model

data class CheckoutRequest(
    val addressId: Int? = null,
    val paymentMethod: String,
    val items: List<CheckoutItem>
)

data class CheckoutItem(
    val productId: Int,
    val quantity: Int
)

data class CheckoutResponse(
    val orderId: Int,
    val orderNumber: String,
    val totalAmount: Double,
    val paymentStatus: String,
    val message: String
)
