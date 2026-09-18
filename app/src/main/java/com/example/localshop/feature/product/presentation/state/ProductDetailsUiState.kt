package com.example.localshop.feature.product.presentation.state

import com.example.localshop.feature.product.domain.model.ProductDetails

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val productDetails: ProductDetails? = null,
    val errorMessage: String? = null
)