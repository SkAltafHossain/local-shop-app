package com.example.localshop.feature.wishlist.data.remote.dto

import com.example.localshop.feature.product.data.remote.dto.ProductDto
import kotlinx.serialization.Serializable

@Serializable
data class WishlistDto(
    val current_page: Int,
    val data: List<WishlistItemDto>,
    val first_page_url: String? = null,
    val from: Int? = null,
    val last_page: Int,
    val last_page_url: String? = null,
    val links: List<LinkDto>? = null,
    val next_page_url: String? = null,
    val path: String? = null,
    val per_page: Int,
    val prev_page_url: String? = null,
    val to: Int? = null,
    val total: Int
)

@Serializable
data class LinkDto(
    val url: String? = null,
    val label: String,
    val page: Int? = null,
    val active: Boolean
)

@Serializable
data class WishlistItemDto(
    val id: Int,
    val user_id: Int? = null,
    val product_id: Int,
    val product: ProductDto,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class WishlistRequestDto(
    val product_id: Int
)
