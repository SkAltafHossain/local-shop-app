package com.example.localshop.feature.product.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String? = null,
    val price: Double,
    val discountPrice: Double? = null,
    val sku: String? = null,
    val stock: Int? = null,
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val imageUrl: String? = null,
    val images: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val isNew: Boolean = false,
    val rating: Double? = null,
    val reviewsCount: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class ProductFilters(
    val category: Int? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val search: String? = null,
    val sort: String? = null,
    val perPage: Int = 12
)