package com.example.localshop.feature.product.domain.usecase

import com.example.localshop.core.result.PagedResult
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.product.domain.model.Product
import com.example.localshop.feature.product.domain.model.ProductFilters
import com.example.localshop.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(filters: ProductFilters): Flow<ResultState<PagedResult<Product>>> {
        return repository.getProducts(filters)
    }
}