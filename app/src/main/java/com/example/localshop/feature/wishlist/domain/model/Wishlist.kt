package com.example.localshop.feature.wishlist.domain.model

import com.example.localshop.feature.product.domain.model.Product

data class Wishlist(
    val items: List<WishlistItem>
)

data class WishlistItem(
    val id: Int,
    val productId: Int,
    val product: Product
)
