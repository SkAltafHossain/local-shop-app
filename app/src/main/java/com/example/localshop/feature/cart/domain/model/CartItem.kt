package com.example.localshop.feature.cart.domain.model

data class CartItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productImage: String? = null,
    val price: Double,
    val discountPrice: Double? = null,
    val quantity: Int,
    val subtotal: Double
)

data class Cart(
    val items: List<CartItem>,
    val subtotal: Double,
    val total: Double
)