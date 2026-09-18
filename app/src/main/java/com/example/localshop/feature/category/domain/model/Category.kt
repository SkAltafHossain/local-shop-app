package com.example.localshop.feature.category.domain.model

data class Category(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val parentId: Int? = null,
    val parentName: String? = null,
    val productsCount: Int? = null,
    val status: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)