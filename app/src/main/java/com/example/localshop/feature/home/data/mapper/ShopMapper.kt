package com.example.localshop.feature.home.data.mapper

import com.example.localshop.feature.home.data.remote.dto.ShopInfoDto
import com.example.localshop.feature.home.data.remote.dto.ShopSettingsDto
import com.example.localshop.feature.home.data.remote.dto.SocialLinksDto
import com.example.localshop.feature.home.domain.model.ShopInfo
import com.example.localshop.feature.home.domain.model.ShopSettings
import com.example.localshop.feature.home.domain.model.SocialLinks

object ShopMapper {
    fun mapToDomain(dto: ShopSettingsDto): ShopSettings {
        return ShopSettings(
            id = dto.id,
            shopName = dto.shopName,
            shopDescription = dto.shopDescription,
            shopEmail = dto.shopEmail,
            shopPhone = dto.shopPhone,
            shopAddress = dto.shopAddress,
            currency = dto.currency,
            logo = dto.logo,
            enableRegistration = dto.enableRegistration,
            enableGuestCheckout = dto.enableGuestCheckout,
            deliveryCharge = dto.deliveryCharge,
            freeDeliveryThreshold = dto.freeDeliveryThreshold,
            taxRate = dto.taxRate,
            socialLinks = dto.socialLinks?.let { mapSocialLinks(it) }
        )
    }
    
    fun mapToDomain(dto: ShopInfoDto): ShopInfo {
        return ShopInfo(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            email = dto.email,
            phone = dto.phone,
            address = dto.address,
            logo = dto.logo,
            coverImage = dto.coverImage,
            socialLinks = dto.socialLinks?.let { mapSocialLinks(it) }
        )
    }
    
    private fun mapSocialLinks(dto: SocialLinksDto): SocialLinks {
        return SocialLinks(
            facebook = dto.facebook,
            twitter = dto.twitter,
            instagram = dto.instagram,
            linkedin = dto.linkedin
        )
    }
}