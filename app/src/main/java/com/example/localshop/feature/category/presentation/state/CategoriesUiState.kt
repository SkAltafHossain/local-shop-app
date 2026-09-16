package com.example.localshop.feature.category.presentation.state

import com.example.localshop.feature.category.domain.model.Category

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val errorMessage: String? = null
)