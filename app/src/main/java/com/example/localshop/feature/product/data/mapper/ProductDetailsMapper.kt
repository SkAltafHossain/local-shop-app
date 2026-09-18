package com.example.localshop.feature.product.data.mapper

import com.example.localshop.feature.product.data.remote.dto.ProductDetailsDto
import com.example.localshop.feature.product.domain.model.ProductDetails

object ProductDetailsMapper {
    fun mapToDomain(dto: ProductDetailsDto): ProductDetails {
        return ProductDetails(
            product = ProductMapper.mapToDomain(dto.product),
            relatedProducts = dto.relatedProducts.map { ProductMapper.mapToDomain(it) }
        )
    }
}