package com.example.localshop.feature.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopInfoDto(
    @SerialName("id")
    val id: Int? = null,
    
    @SerialName("shop_name")
    val shopName: String? = null,
    
    @SerialName("shop_description")
    val shopDescription: String? = null,
    
    @SerialName("shop_email")
    val shopEmail: String? = null,
    
    @SerialName("shop_phone")
    val shopPhone: String? = null,
    
    @SerialName("shop_address")
    val shopAddress: String? = null,
    
    @SerialName("logo")
    val logo: String? = null,
    
    @SerialName("cover_image")
    val coverImage: String? = null,
    
    @SerialName("social_links")
    val socialLinks: SocialLinksDto? = null
)