package com.example.localshop.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("success")
    val success: Boolean,
    
    @SerialName("message")
    val message: String,
    
    @SerialName("data")
    val data: T? = null,
    
    @SerialName("errors")
    val errors: Map<String, List<String>>? = null
)

@Serializable
data class PaginatedResponse<T>(
    @SerialName("success")
    val success: Boolean,
    
    @SerialName("message")
    val message: String,
    
    @SerialName("data")
    val data: List<T>,
    
    @SerialName("pagination")
    val pagination: PaginationMetadata
)

@Serializable
data class PaginationMetadata(
    @SerialName("total")
    val total: Int,
    
    @SerialName("per_page")
    val perPage: Int,
    
    @SerialName("current_page")
    val currentPage: Int,
    
    @SerialName("last_page")
    val lastPage: Int,
    
    @SerialName("from")
    val from: Int? = null,
    
    @SerialName("to")
    val to: Int? = null
)