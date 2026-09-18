package com.example.localshop.feature.category.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.category.domain.model.CategoryWithProducts
import com.example.localshop.feature.category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryWithProductsUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(categoryId: Int, perPage: Int = 12): Flow<ResultState<CategoryWithProducts>> {
        return repository.getCategoryWithProducts(categoryId, perPage)
    }
}