package com.example.localshop.feature.product.domain.repository

import com.example.localshop.core.result.PagedResult
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.product.domain.model.Product
import com.example.localshop.feature.product.domain.model.ProductFilters
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(filters: ProductFilters): Flow<ResultState<PagedResult<Product>>>
    fun getProductDetails(productId: Int): Flow<ResultState<Product>>
    fun searchProducts(query: String, perPage: Int): Flow<ResultState<PagedResult<Product>>>
    fun getFeaturedProducts(perPage: Int): Flow<ResultState<PagedResult<Product>>>
    fun getLatestProducts(perPage: Int): Flow<ResultState<PagedResult<Product>>>
}