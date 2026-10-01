package com.example.localshop.feature.address.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    @SerialName("id")
    val id: Int,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("name")
    val name: String? = null,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("type")
    val type: String = "home",

    @SerialName("address_line1")
    val addressLine1: String,

    @SerialName("address_line2")
    val addressLine2: String? = null,

    @SerialName("city")
    val city: String,

    @SerialName("state")
    val state: String,

    @SerialName("postal_code")
    val postalCode: String,

    @SerialName("country")
    val country: String = "India",

    @SerialName("is_default")
    val isDefault: Boolean = false,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)
