package com.example.localshop.feature.product.data.repository

import com.example.localshop.core.error.ErrorMapper
import com.example.localshop.core.result.PagedResult
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.product.data.mapper.ProductMapper
import com.example.localshop.feature.product.data.remote.ProductApi
import com.example.localshop.feature.product.domain.model.Product
import com.example.localshop.feature.product.domain.model.ProductFilters
import com.example.localshop.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {
    
    override fun getProducts(filters: ProductFilters): Flow<ResultState<PagedResult<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = productApi.getProducts(
                category = filters.category,
                minPrice = filters.minPrice,
                maxPrice = filters.maxPrice,
                search = filters.search,
                sort = filters.sort,
                perPage = filters.perPage
            )
            if (response.success) {
                val products = ProductMapper.mapToDomainList(response.data)
                val pagedResult = PagedResult(
                    items = products,
                    currentPage = response.pagination.currentPage,
                    lastPage = response.pagination.lastPage,
                    total = response.pagination.total,
                    perPage = response.pagination.perPage,
                    from = response.pagination.from,
                    to = response.pagination.to
                )
                emit(ResultState.Success(pagedResult))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
    
    override fun getProductDetails(productId: Int): Flow<ResultState<Product>> = flow {
        emit(ResultState.Loading)
        try {
            val response = productApi.getProductDetails(productId)
            if (response.success && response.data != null) {
                val product = ProductMapper.mapToDomain(response.data)
                emit(ResultState.Success(product))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
    
    override fun searchProducts(query: String, perPage: Int): Flow<ResultState<PagedResult<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = productApi.searchProducts(query, perPage)
            if (response.success) {
                val products = ProductMapper.mapToDomainList(response.data)
                val pagedResult = PagedResult(
                    items = products,
                    currentPage = response.pagination.currentPage,
                    lastPage = response.pagination.lastPage,
                    total = response.pagination.total,
                    perPage = response.pagination.perPage,
                    from = response.pagination.from,
                    to = response.pagination.to
                )
                emit(ResultState.Success(pagedResult))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
    
    override fun getFeaturedProducts(perPage: Int): Flow<ResultState<PagedResult<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = productApi.getFeaturedProducts(perPage)
            if (response.success) {
                val products = ProductMapper.mapToDomainList(response.data)
                val pagedResult = PagedResult(
                    items = products,
                    currentPage = response.pagination.currentPage,
                    lastPage = response.pagination.lastPage,
                    total = response.pagination.total,
                    perPage = response.pagination.perPage,
                    from = response.pagination.from,
                    to = response.pagination.to
                )
                emit(ResultState.Success(pagedResult))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
    
    override fun getLatestProducts(perPage: Int): Flow<ResultState<PagedResult<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = productApi.getLatestProducts(perPage)
            if (response.success) {
                val products = ProductMapper.mapToDomainList(response.data)
                val pagedResult = PagedResult(
                    items = products,
                    currentPage = response.pagination.currentPage,
                    lastPage = response.pagination.lastPage,
                    total = response.pagination.total,
                    perPage = response.pagination.perPage,
                    from = response.pagination.from,
                    to = response.pagination.to
                )
                emit(ResultState.Success(pagedResult))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
}