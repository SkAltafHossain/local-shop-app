package com.example.localshop.feature.home.data.remote.dto

import com.example.localshop.feature.category.data.remote.dto.CategoryDto
import com.example.localshop.feature.product.data.remote.dto.ProductDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeApiResponseDto(
    @SerialName("products")
    val products: List<ProductDto>,
    
    @SerialName("categories")
    val categories: List<CategoryDto>,
    
    @SerialName("latest_products")
    val latestProducts: List<ProductDto>,
    
    @SerialName("featured_products")
    val featuredProducts: List<ProductDto>
)
