package com.example.localshop.feature.wishlist.data.mapper

import com.example.localshop.core.common.Constants
import com.example.localshop.feature.product.data.mapper.ProductMapper
import com.example.localshop.feature.wishlist.data.remote.dto.WishlistDto
import com.example.localshop.feature.wishlist.data.remote.dto.WishlistItemDto
import com.example.localshop.feature.wishlist.domain.model.Wishlist
import com.example.localshop.feature.wishlist.domain.model.WishlistItem

object WishlistMapper {
    fun mapToDomain(dto: WishlistDto): Wishlist {
        return Wishlist(
            items = dto.data.map { mapItemToDomain(it) }
        )
    }

    fun mapItemToDomain(dto: WishlistItemDto): WishlistItem {
        return WishlistItem(
            id = dto.id,
            productId = dto.product_id,
            product = ProductMapper.mapToDomain(dto.product)
        )
    }
}
