package com.example.localshop.feature.category.domain.repository

import com.example.localshop.core.result.PagedResult
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.category.domain.model.Category
import com.example.localshop.feature.product.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<ResultState<List<Category>>>
    fun getCategoryDetails(categoryId: Int): Flow<ResultState<Category>>
    fun getCategoryProducts(categoryId: Int, perPage: Int): Flow<ResultState<PagedResult<Product>>>
}