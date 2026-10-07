package com.example.localshop.feature.orders.domain.model

data class Order(
    val id: Int,
    val orderId: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val orderStatus: OrderStatus,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val customerAddress: String,
    val addressId: Int,
    val items: List<OrderItem>,
    val createdAt: String? = null,
    val updatedAt: String? = null
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

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    companion object {
        fun fromString(value: String): OrderStatus {
            return values().find { it.name.equals(value, ignoreCase = true) } ?: PENDING
        }
    }
}
