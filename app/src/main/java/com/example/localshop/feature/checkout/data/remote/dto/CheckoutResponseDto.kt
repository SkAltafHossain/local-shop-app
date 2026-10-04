package com.example.localshop.feature.checkout.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponseDto(
    @SerialName("order_id")
    val orderId: String,

    @SerialName("total_amount")
    val totalAmount: String,

    @SerialName("payment_method")
    val paymentMethod: String,

    @SerialName("order_status")
    val orderStatus: String,

    @SerialName("customer_name")
    val customerName: String,

    @SerialName("customer_email")
    val customerEmail: String,

    @SerialName("customer_phone")
    val customerPhone: String,

    @SerialName("customer_address")
    val customerAddress: String,

    @SerialName("address_id")
    val addressId: Int,

    @SerialName("id")
    val id: Int,

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
