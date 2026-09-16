package com.example.localshop.feature.search.presentation.state

import com.example.localshop.feature.product.domain.model.Product

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val errorMessage: String? = null
)