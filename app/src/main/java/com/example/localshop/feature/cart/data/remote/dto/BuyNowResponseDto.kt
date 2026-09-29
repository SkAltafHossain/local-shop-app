package com.example.localshop.feature.cart.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuyNowResponseDto(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("data")
    val data: BuyNowDataDto
)

@Serializable
data class BuyNowDataDto(
    @SerialName("product")
    val product: BuyNowProductDto,

    @SerialName("quantity")
    val quantity: Int,

    @SerialName("price")
    val price: String,

    @SerialName("subtotal")
    val subtotal: Double,

    @SerialName("shipping")
    val shipping: Double,

    @SerialName("total")
    val total: Double,

    @SerialName("is_buy_now")
    val isBuyNow: Boolean
)

@Serializable
data class BuyNowProductDto(
    @SerialName("id")
    val id: Int,

    @SerialName("category_id")
    val categoryId: Int,

    @SerialName("name")
    val name: String,

    @SerialName("slug")
    val slug: String,

    @SerialName("description")
    val description: String,

    @SerialName("price")
    val price: String,

    @SerialName("discount_price")
    val discountPrice: String?,

    @SerialName("stock")
    val stock: Int,

    @SerialName("image")
    val image: String,

    @SerialName("status")
    val status: String,

    @SerialName("featured")
    val featured: Boolean,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String?,

    @SerialName("deleted_at")
    val deletedAt: String?,

    @SerialName("category")
    val category: CategoryDto
)

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("slug")
    val slug: String,

    @SerialName("image")
    val image: String,

    @SerialName("status")
    val status: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("deleted_at")
    val deletedAt: String?
)
