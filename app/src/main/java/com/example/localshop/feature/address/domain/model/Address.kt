package com.example.localshop.feature.address.domain.model

data class Address(
    val id: Int,
    val userId: Int,
    val fullName: String,
    val phone: String,
    val addressLine1: String,
    val addressLine2: String? = null,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String = "India",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val type: String = "home",
    val isDefault: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
