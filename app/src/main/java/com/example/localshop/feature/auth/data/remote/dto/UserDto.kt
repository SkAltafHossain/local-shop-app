package com.example.localshop.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("email")
    val email: String,
    
    @SerialName("email_verified_at")
    val emailVerifiedAt: String? = null,
    
    @SerialName("phone")
    val phone: String? = null,
    
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("is_admin")
    val isAdmin: Int? = null,
    
    @SerialName("avatar")
    val avatar: String? = null,
    
    @SerialName("created_at")
    val createdAt: String? = null,
    
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class AuthResponseDto(
    @SerialName("token")
    val token: String,
    
    @SerialName("token_type")
    val tokenType: String? = null,
    
    @SerialName("user")
    val user: UserDto
)