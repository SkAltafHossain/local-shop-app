package com.example.localshop.feature.cart.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
    @SerialName("id")
    val id: Int,

    @SerialName("product_id")
    val productId: Int,

    @SerialName("product")
    val product: ProductDto,

    @SerialName("quantity")
    val quantity: Int,

    @SerialName("subtotal")
    val subtotal: Double
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
    val discountPrice: String? = null,

    @SerialName("image")
    val image: String? = null
)
