package com.example.localshop.feature.category.data.mapper

import com.example.localshop.feature.category.data.remote.dto.CategoryDto
import com.example.localshop.feature.category.domain.model.Category

object CategoryMapper {
    fun mapToDomain(dto: CategoryDto): Category {
        return Category(
            id = dto.id,
            name = dto.name,
            slug = dto.slug,
            description = dto.description,
            imageUrl = dto.imageUrl,
            parentId = dto.parentId,
            parentName = dto.parentName,
            productsCount = dto.productsCount,
            isActive = dto.isActive,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
    
    fun mapToDomainList(dtos: List<CategoryDto>): List<Category> {
        return dtos.map { mapToDomain(it) }
    }
}