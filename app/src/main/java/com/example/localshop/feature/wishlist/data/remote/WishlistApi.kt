package com.example.localshop.feature.wishlist.data.remote

import com.example.localshop.core.network.ApiResponse
import com.example.localshop.feature.wishlist.data.remote.dto.WishlistDto
import com.example.localshop.feature.wishlist.data.remote.dto.WishlistItemDto
import com.example.localshop.feature.wishlist.data.remote.dto.WishlistRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WishlistApi {
    @GET("wishlist")
    suspend fun getWishlist(): ApiResponse<WishlistDto>

    @POST("wishlist")
    suspend fun addToWishlist(@Body request: WishlistRequestDto): ApiResponse<WishlistItemDto>

    @DELETE("wishlist/{id}")
    suspend fun removeFromWishlist(@Path("id") wishlistItemId: Int): ApiResponse<Unit>

    @POST("wishlist/check")
    suspend fun checkInWishlist(@Body request: WishlistRequestDto): ApiResponse<Map<String, Boolean>>
}
