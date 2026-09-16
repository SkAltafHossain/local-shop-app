package com.example.localshop.feature.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopSettingsDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("shop_name")
    val shopName: String,
    
    @SerialName("shop_description")
    val shopDescription: String? = null,
    
    @SerialName("shop_email")
    val shopEmail: String? = null,
    
    @SerialName("shop_phone")
    val shopPhone: String? = null,
    
    @SerialName("shop_address")
    val shopAddress: String? = null,
    
    @SerialName("currency")
    val currency: String = "USD",
    
    @SerialName("logo")
    val logo: String? = null,
    
    @SerialName("enable_registration")
    val enableRegistration: Boolean = true,
    
    @SerialName("enable_guest_checkout")
    val enableGuestCheckout: Boolean = false,
    
    @SerialName("delivery_charge")
    val deliveryCharge: Double? = null,
    
    @SerialName("free_delivery_threshold")
    val freeDeliveryThreshold: Double? = null,
    
    @SerialName("tax_rate")
    val taxRate: Double? = null,
    
    @SerialName("social_links")
    val socialLinks: SocialLinksDto? = null
)

@Serializable
data class SocialLinksDto(
    @SerialName("facebook")
    val facebook: String? = null,
    
    @SerialName("twitter")
    val twitter: String? = null,
    
    @SerialName("instagram")
    val instagram: String? = null,
    
    @SerialName("linkedin")
    val linkedin: String? = null
)