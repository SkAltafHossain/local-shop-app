package com.example.localshop.feature.home.domain.model

data class ShopInfo(
    val id: Int,
    val name: String,
    val description: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val logo: String? = null,
    val coverImage: String? = null,
    val socialLinks: SocialLinks? = null
)