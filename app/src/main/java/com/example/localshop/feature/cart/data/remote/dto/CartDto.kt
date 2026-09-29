package com.example.localshop.feature.cart.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartDto(
    @SerialName("items")
    val items: List<CartItemDto>,

    @SerialName("total")
    val total: Double,

    @SerialName("count")
    val count: Int
)
