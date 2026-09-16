package com.example.localshop.feature.auth.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String? = null,
    val avatar: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class AuthResponse(
    val token: String,
    val user: User
)