package com.example.localshop.feature.cart.domain.model

data class BuyNowData(
    val product: Product,
    val quantity: Int,
    val price: Double,
    val subtotal: Double,
    val shipping: Double,
    val total: Double,
    val isBuyNow: Boolean
)

data class Product(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val slug: String,
    val description: String,
    val price: Double,
    val discountPrice: Double?,
    val stock: Int,
    val imageUrl: String,
    val status: String,
    val featured: Boolean,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
    val category: Category
)

data class Category(
    val id: Int,
    val name: String,
    val slug: String,
    val image: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)
