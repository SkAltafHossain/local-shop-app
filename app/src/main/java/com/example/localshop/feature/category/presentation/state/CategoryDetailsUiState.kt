package com.example.localshop.feature.category.presentation.state

import com.example.localshop.feature.category.domain.model.CategoryWithProducts

data class CategoryDetailsUiState(
    val isLoading: Boolean = false,
    val categoryWithProducts: CategoryWithProducts? = null,
    val errorMessage: String? = null
)