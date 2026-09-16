package com.example.localshop.feature.home.domain.model

data class ShopSettings(
    val id: Int,
    val shopName: String,
    val shopDescription: String? = null,
    val shopEmail: String? = null,
    val shopPhone: String? = null,
    val shopAddress: String? = null,
    val currency: String = "USD",
    val logo: String? = null,
    val enableRegistration: Boolean = true,
    val enableGuestCheckout: Boolean = false,
    val deliveryCharge: Double? = null,
    val freeDeliveryThreshold: Double? = null,
    val taxRate: Double? = null,
    val socialLinks: SocialLinks? = null
)

data class SocialLinks(
    val facebook: String? = null,
    val twitter: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null
)