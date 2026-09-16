package com.example.localshop.feature.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShopInfoDto(
    @SerialName("id")
    val id: Int,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("email")
    val email: String? = null,
    
    @SerialName("phone")
    val phone: String? = null,
    
    @SerialName("address")
    val address: String? = null,
    
    @SerialName("logo")
    val logo: String? = null,
    
    @SerialName("cover_image")
    val coverImage: String? = null,
    
    @SerialName("social_links")
    val socialLinks: SocialLinksDto? = null
)