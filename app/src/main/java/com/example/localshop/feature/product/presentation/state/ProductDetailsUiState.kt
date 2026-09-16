package com.example.localshop.feature.product.presentation.state

import com.example.localshop.feature.product.domain.model.Product

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val errorMessage: String? = null
)