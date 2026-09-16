package com.example.localshop.feature.home.data.repository

import com.example.localshop.core.error.ErrorMapper
import com.example.localshop.core.result.ResultState
import com.example.localshop.feature.home.data.mapper.ShopMapper
import com.example.localshop.feature.home.data.remote.ShopApi
import com.example.localshop.feature.home.domain.model.ShopInfo
import com.example.localshop.feature.home.domain.model.ShopSettings
import com.example.localshop.feature.home.domain.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShopRepositoryImpl @Inject constructor(
    private val shopApi: ShopApi
) : ShopRepository {
    
    override fun getShopSettings(): Flow<ResultState<ShopSettings>> = flow {
        emit(ResultState.Loading)
        try {
            val response = shopApi.getShopSettings()
            if (response.success && response.data != null) {
                val shopSettings = ShopMapper.mapToDomain(response.data)
                emit(ResultState.Success(shopSettings))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
    
    override fun getShopInfo(): Flow<ResultState<ShopInfo>> = flow {
        emit(ResultState.Loading)
        try {
            val response = shopApi.getShopInfo()
            if (response.success && response.data != null) {
                val shopInfo = ShopMapper.mapToDomain(response.data)
                emit(ResultState.Success(shopInfo))
            } else {
                emit(ResultState.Error(response.message ?: "Operation failed"))
            }
        } catch (e: Exception) {
            val appError = ErrorMapper.mapToAppError(e)
            emit(ResultState.Error(appError.localizedMessage ?: appError.toString(), appError))
        }
    }
}