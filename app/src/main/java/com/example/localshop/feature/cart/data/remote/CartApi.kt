package com.example.localshop.feature.cart.data.remote

import com.example.localshop.core.network.ApiResponse
import com.example.localshop.feature.cart.data.remote.dto.AddToCartResponseDto
import com.example.localshop.feature.cart.data.remote.dto.BuyNowRequestDto
import com.example.localshop.feature.cart.data.remote.dto.BuyNowResponseDto
import com.example.localshop.feature.cart.data.remote.dto.CartDto
import com.example.localshop.feature.cart.data.remote.dto.CartItemDto
import com.example.localshop.feature.cart.data.remote.dto.CartRequestDto
import com.example.localshop.feature.cart.data.remote.dto.CartUpdateRequestDto
import com.example.localshop.feature.checkout.data.remote.dto.CheckoutRequestDto
import com.example.localshop.feature.checkout.data.remote.dto.CheckoutResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartApi {
    @POST("cart")
    suspend fun addToCart(@Body request: CartRequestDto): ApiResponse<AddToCartResponseDto>
    
    @GET("cart")
    suspend fun getCart(): ApiResponse<CartDto>
    
    @PUT("cart/{id}")
    suspend fun updateCartItem(
        @Path("id") cartItemId: Int,
        @Body request: CartUpdateRequestDto
    ): ApiResponse<Unit>
    
    @DELETE("cart/{id}")
    suspend fun deleteCartItem(@Path("id") cartItemId: Int): ApiResponse<Unit>
    
    @DELETE("cart")
    suspend fun clearCart(): ApiResponse<Unit>

    @POST("cart/buy-now")
    suspend fun buyNow(@Body request: BuyNowRequestDto): ApiResponse<BuyNowResponseDto>

    @POST("orders")
    suspend fun checkout(@Body request: CheckoutRequestDto): ApiResponse<CheckoutResponseDto>
}
