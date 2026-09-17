package com.example.localshop.feature.product.data.mapper

import com.example.localshop.feature.product.data.remote.dto.ProductDto
import com.example.localshop.feature.product.domain.model.Product

object ProductMapper {
    fun mapToDomain(dto: ProductDto): Product {
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
            imageUrl = dto.imageUrl,
            images = dto.images,
            isFeatured = dto.isFeaturedAlt ?: dto.isFeatured,
            isNew = dto.isNew,
            rating = dto.rating,
            reviewsCount = dto.reviewsCount,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
    
    fun mapToDomainList(dtos: List<ProductDto>): List<Product> {
        return dtos.map { mapToDomain(it) }
    }
}