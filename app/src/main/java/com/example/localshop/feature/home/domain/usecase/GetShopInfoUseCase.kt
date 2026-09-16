package com.example.localshop.feature.home.domain.usecase

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.domain.model.ShopInfo
import com.example.localshop.feature.home.domain.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopInfoUseCase @Inject constructor(
    private val repository: ShopRepository
) {
    operator fun invoke(): Flow<ResultState<ShopInfo>> {
        return repository.getShopInfo()
    }
}