package com.example.localshop.feature.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedOrdersResponse(
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("data")
    val orders: List<OrderDto>,
    @SerialName("total")
    val total: Int
)

@Serializable
data class OrderDto(
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("address_id")
    val addressId: Int,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("customer_name")
    val customerName: String,
    @SerialName("customer_email")
    val customerEmail: String,
    @SerialName("customer_phone")
    val customerPhone: String,
    @SerialName("customer_address")
    val customerAddress: String,
    @SerialName("subtotal")
    val subtotal: String,
    @SerialName("shipping")
    val shipping: String,
    @SerialName("total_amount")
    val totalAmount: String,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("payment_status")
    val paymentStatus: String,
    @SerialName("order_status")
    val orderStatus: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("items")
    val items: List<OrderItemDto>
)

@Serializable
data class OrderItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("order_id")
    val orderId: Int,
    @SerialName("product_id")
    val productId: Int,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("price")
    val price: String,
    @SerialName("total")
    val total: String,
    @SerialName("product")
    val product: ProductDto
)

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: String,
    @SerialName("discount_price")
    val discountPrice: String,
    @SerialName("image")
    val image: String
)
