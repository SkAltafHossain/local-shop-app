package com.example.localshop.feature.category.domain.model

import com.example.localshop.feature.product.domain.model.Product

data class CategoryWithProducts(
    val category: Category,
    val products: List<Product>
)