package com.example.localshop.feature.home.domain.repository

import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.domain.model.ShopInfo
import com.example.localshop.feature.home.domain.model.ShopSettings
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    fun getShopSettings(): Flow<ResultState<ShopSettings>>
    fun getShopInfo(): Flow<ResultState<ShopInfo>>
}