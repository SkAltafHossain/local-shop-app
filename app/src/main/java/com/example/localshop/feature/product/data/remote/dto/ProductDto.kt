package com.example.localshop.feature.product.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("slug")
    val slug: String? = null,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("price")
    val price: Double,
    
    @SerialName("discount_price")
    val discountPrice: Double? = null,
    
    @SerialName("sku")
    val sku: String? = null,
    
    @SerialName("stock")
    val stock: Int? = null,
    
    @SerialName("category_id")
    val categoryId: Int? = null,
    
    @SerialName("category_name")
    val categoryName: String? = null,
    
    @SerialName("category")
    val category: CategoryNestedDto? = null,
    
    @SerialName("image")
    val imageUrl: String? = null,
    
    @SerialName("images")
    val images: List<String> = emptyList(),
    
    @SerialName("featured")
    val isFeatured: Boolean = false,
    
    @SerialName("is_featured")
    val isFeaturedAlt: Boolean? = null,
    
    @SerialName("is_new")
    val isNew: Boolean = false,
    
    @SerialName("rating")
    val rating: Double? = null,
    
    @SerialName("reviews_count")
    val reviewsCount: Int? = null,
    
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("created_at")
    val createdAt: String? = null,
    
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class CategoryNestedDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("slug")
    val slug: String
)