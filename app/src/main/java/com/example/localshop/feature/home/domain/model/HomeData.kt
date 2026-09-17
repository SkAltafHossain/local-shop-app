package com.example.localshop.feature.home.domain.model

import com.example.localshop.feature.category.domain.model.Category
import com.example.localshop.feature.product.domain.model.Product

data class HomeData(
    val products: List<Product>,
    val categories: List<Category>,
    val latestProducts: List<Product>,
    val featuredProducts: List<Product>
)
