package com.example.localshop.feature.address.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressRequestDto(
    @SerialName("name")
    val name: String,

    @SerialName("phone")
    val phone: String,

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

    @SerialName("latitude")
    val latitude: Double? = null,

    @SerialName("longitude")
    val longitude: Double? = null
)
