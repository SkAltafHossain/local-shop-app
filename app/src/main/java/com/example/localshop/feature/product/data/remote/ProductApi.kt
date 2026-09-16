package com.example.localshop.feature.product.data.remote

import com.example.localshop.core.network.ApiResponse
import com.example.localshop.core.network.PaginatedResponse
import com.example.localshop.feature.product.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: Int? = null,
        @Query("min_price") minPrice: Double? = null,
        @Query("max_price") maxPrice: Double? = null,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null,
        @Query("per_page") perPage: Int = 12
    ): PaginatedResponse<ProductDto>
    
    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") productId: Int): ApiResponse<ProductDto>
    
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 12
    ): PaginatedResponse<ProductDto>
    
    @GET("products/featured")
    suspend fun getFeaturedProducts(
        @Query("per_page") perPage: Int = 12
    ): PaginatedResponse<ProductDto>
    
    @GET("products/latest")
    suspend fun getLatestProducts(
        @Query("per_page") perPage: Int = 12
    ): PaginatedResponse<ProductDto>
}