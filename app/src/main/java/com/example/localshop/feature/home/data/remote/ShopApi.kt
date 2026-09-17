package com.example.localshop.feature.home.data.remote

import com.example.localshop.core.network.ApiResponse
import com.example.localshop.feature.home.data.remote.dto.HomeApiResponseDto
import com.example.localshop.feature.home.data.remote.dto.ShopInfoDto
import com.example.localshop.feature.home.data.remote.dto.ShopSettingsDto
import retrofit2.http.GET

interface ShopApi {
    @GET("shop/settings")
    suspend fun getShopSettings(): ApiResponse<ShopSettingsDto>
    
    @GET("shop/info")
    suspend fun getShopInfo(): ApiResponse<ShopInfoDto>
    
    @GET("home")
    suspend fun getHomeData(): ApiResponse<HomeApiResponseDto>
}