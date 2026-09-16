package com.example.localshop.feature.category.domain.usecase

import com.example.localshop.core.result.PagedResult
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.category.domain.repository.CategoryRepository
import com.example.localshop.feature.product.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryProductsUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(categoryId: Int, perPage: Int = 12): Flow<ResultState<PagedResult<Product>>> {
        return repository.getCategoryProducts(categoryId, perPage)
    }
}