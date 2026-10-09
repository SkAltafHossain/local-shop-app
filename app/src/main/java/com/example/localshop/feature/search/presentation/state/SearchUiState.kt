package com.example.localshop.feature.search.presentation.state

import com.example.localshop.feature.product.domain.model.Product

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val errorMessage: String? = null,
    val isAddingToCart: Boolean = false,
    val addToCartSuccess: Boolean = false,
    val addToCartMessage: String? = null,
    val cartProductIds: Set<Int> = emptySet(),
    val wishlistProductIds: Set<Int> = emptySet(),
    val wishlistItems: Map<Int, Int> = emptyMap() // productId to wishlistItemId
)