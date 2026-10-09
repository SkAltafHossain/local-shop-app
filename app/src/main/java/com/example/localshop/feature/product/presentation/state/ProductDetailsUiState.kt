package com.example.localshop.feature.product.presentation.state

import com.example.localshop.feature.product.domain.model.ProductDetails

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val productDetails: ProductDetails? = null,
    val errorMessage: String? = null,
    val isAddingToCart: Boolean = false,
    val addToCartSuccess: Boolean = false,
    val addToCartMessage: String? = null,
    val isInCart: Boolean = false,
    val cartProductIds: Set<Int> = emptySet(),
    val isInWishlist: Boolean = false,
    val wishlistItemId: Int? = null
)