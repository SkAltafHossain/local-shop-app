package com.example.localshop.feature.checkout.domain.model

data class CheckoutRequest(
    val addressId: Int? = null,
    val paymentMethod: String,
    val isBuyNow: Boolean = false,
    val items: List<CheckoutItem>
)

data class CheckoutItem(
    val productId: Int,
    val quantity: Int
)

data class CheckoutResponse(
    val orderId: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val orderStatus: String,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val customerAddress: String,
    val addressId: Int,
    val id: Int,
    val items: List<OrderItem>
)

data class OrderItem(
    val id: Int,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val price: Double,
    val total: Double,
    val productName: String,
    val productPrice: Double,
    val productDiscountPrice: Double,
    val productImage: String
)
