package com.example.localshop.feature.home.data.mapper

import com.example.localshop.feature.category.data.mapper.CategoryMapper
import com.example.localshop.feature.home.data.remote.dto.HomeApiResponseDto
import com.example.localshop.feature.home.domain.model.HomeData
import com.example.localshop.feature.product.data.mapper.ProductMapper

object HomeMapper {
    fun mapToDomain(dto: HomeApiResponseDto): HomeData {
        return HomeData(
            products = ProductMapper.mapToDomainList(dto.products),
            categories = CategoryMapper.mapToDomainList(dto.categories),
            latestProducts = ProductMapper.mapToDomainList(dto.latestProducts),
            featuredProducts = ProductMapper.mapToDomainList(dto.featuredProducts)
        )
    }
}
