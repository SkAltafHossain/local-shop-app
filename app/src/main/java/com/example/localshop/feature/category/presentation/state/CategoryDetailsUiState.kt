package com.example.localshop.feature.category.presentation.state

import com.example.localshop.feature.category.domain.model.CategoryWithProducts

data class CategoryDetailsUiState(
    val isLoading: Boolean = false,
    val categoryWithProducts: CategoryWithProducts? = null,
    val errorMessage: String? = null,
    val isAddingToCart: Boolean = false,
    val addToCartSuccess: Boolean = false,
    val addToCartMessage: String? = null,
    val cartProductIds: Set<Int> = emptySet(),
    val wishlistProductIds: Set<Int> = emptySet(),
    val wishlistItems: Map<Int, Int> = emptyMap() // productId to wishlistItemId
)