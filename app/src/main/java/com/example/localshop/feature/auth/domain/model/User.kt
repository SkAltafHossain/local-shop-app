package com.example.localshop.feature.auth.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val emailVerifiedAt: String? = null,
    val phone: String? = null,
    val status: String? = null,
    val isAdmin: Int = 0,
    val avatar: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class AuthResponse(
    val token: String,
    val tokenType: String? = null,
    val user: User
)