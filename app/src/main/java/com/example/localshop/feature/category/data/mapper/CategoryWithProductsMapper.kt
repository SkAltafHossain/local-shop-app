package com.example.localshop.feature.category.data.mapper

import com.example.localshop.feature.category.data.remote.dto.CategoryWithProductsDto
import com.example.localshop.feature.category.domain.model.CategoryWithProducts
import com.example.localshop.feature.product.data.mapper.ProductMapper

object CategoryWithProductsMapper {
    fun mapToDomain(dto: CategoryWithProductsDto): CategoryWithProducts {
        return CategoryWithProducts(
            category = CategoryMapper.mapToDomain(dto.category),
            products = dto.products.map { ProductMapper.mapToDomain(it) }
        )
    }
}