package com.example.localshop.feature.category.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("slug")
    val slug: String,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("image")
    val imageUrl: String? = null,
    
    @SerialName("parent_id")
    val parentId: Int? = null,
    
    @SerialName("parent_name")
    val parentName: String? = null,
    
    @SerialName("products_count")
    val productsCount: Int? = null,
    
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("created_at")
    val createdAt: String? = null,
    
    @SerialName("updated_at")
    val updatedAt: String? = null
)