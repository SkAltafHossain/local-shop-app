package com.example.localshop.feature.product.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsDto(
    @SerialName("product")
    val product: ProductDto,
    
    @SerialName("related_products")
    val relatedProducts: List<ProductDto>
)