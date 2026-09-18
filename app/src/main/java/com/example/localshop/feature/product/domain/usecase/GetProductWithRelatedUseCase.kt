package com.example.localshop.feature.product.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.product.domain.model.ProductDetails
import com.example.localshop.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductWithRelatedUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(productId: Int): Flow<ResultState<ProductDetails>> {
        return repository.getProductWithRelated(productId)
    }
}