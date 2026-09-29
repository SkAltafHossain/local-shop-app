package com.example.localshop.feature.cart.data.mapper

import com.example.localshop.core.common.Constants
import com.example.localshop.feature.cart.data.remote.dto.AddToCartResponseDto
import com.example.localshop.feature.cart.data.remote.dto.CartDto
import com.example.localshop.feature.cart.data.remote.dto.CartItemDto
import com.example.localshop.feature.cart.domain.model.Cart
import com.example.localshop.feature.cart.domain.model.CartItem

object CartMapper {
    fun toDomain(dto: CartDto): Cart {
        // Calculate subtotal from items (sum of original prices before discount)
        val calculatedSubtotal = dto.items.sumOf { item ->
            val originalPrice = item.product.price.toDoubleOrNull() ?: 0.0
            originalPrice * item.quantity
        }

        return Cart(
            items = dto.items.map { toDomain(it) },
            subtotal = calculatedSubtotal,
            total = dto.total,
            cartCount = dto.count
        )
    }

    fun toDomain(dto: CartItemDto): CartItem {
        val imagePath = dto.product.image
        val fullImageUrl = if (!imagePath.isNullOrBlank()) {
            // Check if it's already a full URL
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                imagePath
            } else {
                "${Constants.IMAGE_BASE_URL}${imagePath}"
            }
        } else {
            null
        }

        val price = dto.product.price.toDoubleOrNull() ?: 0.0
        val discountPrice = dto.product.discountPrice?.toDoubleOrNull()
        // Calculate subtotal based on whether there's a discount
        val effectivePrice = discountPrice ?: price
        val calculatedSubtotal = effectivePrice * dto.quantity

        return CartItem(
            id = dto.id,
            productId = dto.productId,
            productName = dto.product.name,
            productImage = fullImageUrl,
            price = price,
            discountPrice = discountPrice,
            quantity = dto.quantity,
            subtotal = calculatedSubtotal
        )
    }

    fun toDomainFromAddToCart(dto: AddToCartResponseDto): CartItem {
        // The add to cart response doesn't include product details
        // Return a minimal CartItem with available fields
        return CartItem(
            id = dto.cartItem.id,
            productId = dto.cartItem.productId,
            productName = "", // Not provided in response
            productImage = null, // Not provided in response
            price = 0.0, // Not provided in response
            discountPrice = null,
            quantity = dto.cartItem.quantity,
            subtotal = 0.0 // Not provided in response
        )
    }
}
