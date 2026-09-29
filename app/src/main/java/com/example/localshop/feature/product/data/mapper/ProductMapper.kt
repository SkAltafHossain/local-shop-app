package com.example.localshop.feature.product.data.mapper

import com.example.localshop.core.common.Constants
import com.example.localshop.feature.product.data.remote.dto.ProductDto
import com.example.localshop.feature.product.domain.model.Category
import com.example.localshop.feature.product.domain.model.Product

object ProductMapper {
    fun mapToDomain(dto: ProductDto): Product {
        val fullImageUrl = if (!dto.imageUrl.isNullOrBlank()) {
            // Check if it's already a full URL
            if (dto.imageUrl.startsWith("http://") || dto.imageUrl.startsWith("https://")) {
                dto.imageUrl
            } else {
                "${Constants.IMAGE_BASE_URL}${dto.imageUrl}"
            }
        } else {
            null
        }

        val fullImages = dto.images.map { imagePath ->
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                imagePath
            } else {
                "${Constants.IMAGE_BASE_URL}${imagePath}"
            }
        }

        return Product(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            price = dto.price,
            discountPrice = dto.discountPrice,
            sku = dto.sku,
            stock = dto.stock,
            categoryId = dto.categoryId ?: dto.category?.id,
            categoryName = dto.categoryName ?: dto.category?.name,
            imageUrl = fullImageUrl,
            images = fullImages,
            isFeatured = dto.isFeaturedAlt ?: dto.isFeatured,
            isNew = dto.isNew,
            rating = dto.rating,
            reviewsCount = dto.reviewsCount,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            slug = dto.slug,
            category = dto.category?.let {
                Category(it.id, it.name, it.slug)
            },
            featured = dto.isFeaturedAlt,
            status = dto.status
        )
    }

    fun mapToDomainList(dtos: List<ProductDto>): List<Product> {
        return dtos.map { mapToDomain(it) }
    }
}