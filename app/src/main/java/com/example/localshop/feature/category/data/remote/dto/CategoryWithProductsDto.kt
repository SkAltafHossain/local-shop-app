package com.example.localshop.feature.category.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryWithProductsDto(
    @SerialName("category")
    val category: CategoryDto,
    
    @SerialName("products")
    val products: List<com.example.localshop.feature.product.data.remote.dto.ProductDto>
)