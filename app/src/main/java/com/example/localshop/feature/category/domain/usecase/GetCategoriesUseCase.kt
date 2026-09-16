package com.example.localshop.feature.category.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.category.domain.model.Category
import com.example.localshop.feature.category.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<ResultState<List<Category>>> {
        return repository.getCategories()
    }
}