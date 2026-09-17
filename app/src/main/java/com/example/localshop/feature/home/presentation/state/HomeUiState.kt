package com.example.localshop.feature.home.presentation.state

import com.example.localshop.feature.category.domain.model.Category
import com.example.localshop.feature.home.domain.model.ShopInfo
import com.example.localshop.feature.home.domain.model.ShopSettings
import com.example.localshop.feature.product.domain.model.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val shopSettings: ShopSettings? = null,
    val shopInfo: ShopInfo? = null,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val latestProducts: List<Product> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val errorMessage: String? = null
)