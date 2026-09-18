package com.example.localshop.feature.product.domain.model

data class ProductDetails(
    val product: Product,
    val relatedProducts: List<Product>
)